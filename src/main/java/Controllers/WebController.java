package Controllers;

import Entity.QuestionEntity;
import Entity.QuizEntity;
import Services.QuestionService;
import Services.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/")
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    private final QuizService quizService;
    private final QuestionService questionService;
    @Autowired
    public WebController(QuizService quizService, QuestionService questionService) {
        this.quizService = quizService;
        this.questionService = questionService;
    }
    @GetMapping
    public String home(Model model) {
        logger.info("Loading home page");
        List<QuizEntity> quizzes = quizService.findAll();
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("quizCount", quizzes.size());
        return "index";
    }
    @GetMapping("/quizzes")
    public String listQuizzes(Model model) {
        logger.info("Loading quiz list");
        List<QuizEntity> quizzes = quizService.findAll();
        model.addAttribute("quizzes", quizzes);
        return "quiz/list";
    }
    @GetMapping("/quiz/{id}")
    public String viewQuiz(@PathVariable Long id, Model model) {
        logger.info("Viewing quiz with ID: {}", id);
        Optional<QuizEntity> quiz = quizService.findById(id);
        if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
            model.addAttribute("questions", quiz.get().getQuestions());
            return "quiz/view";
        }
        return "redirect:/quizzes?error=Quiz not found";
    }
    @GetMapping("/quiz/{id}/take")
    public String takeQuiz(@PathVariable Long id, Model model) {
        logger.info("Starting quiz with ID: {}", id);
        Optional<QuizEntity> quiz = quizService.findById(id);
        if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
            model.addAttribute("questions", quiz.get().getQuestions());
            model.addAttribute("userAnswers", new HashMap<Long, String>());
            return "quiz/take";
        }
        return "redirect:/quizzes?error=Quiz not found";
    }
    @PostMapping("/quiz/{id}/submit")
    public String submitQuiz(@PathVariable Long id,
                             @RequestParam Map<String, String> answers,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        logger.info("Submitting quiz with ID: {}", id);
        Optional<QuizEntity> quizOpt = quizService.findById(id);
        if (quizOpt.isEmpty()) {
            return "redirect:/quizzes?error=Quiz not found";
        }
        QuizEntity quiz = quizOpt.get();
        List<QuestionEntity> questions = quiz.getQuestions();
        int correctCount = 0;
        int totalScore = 0;
        int maxScore = 0;
        List<Map<String, Object>> results = new ArrayList<>();
        for (QuestionEntity question : questions) {
            String userAnswer = answers.get("answer_" + question.getId());
            boolean isCorrect = question.isCorrectAnswer(userAnswer);
            Map<String, Object> result = new HashMap<>();
            result.put("question", question);
            result.put("userAnswer", userAnswer);
            result.put("isCorrect", isCorrect);
            results.add(result);
            maxScore += question.getScore();
            if (isCorrect) {
                correctCount++;
                totalScore += question.getScore();
            }
        }
        model.addAttribute("quiz", quiz);
        model.addAttribute("results", results);
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("totalQuestions", questions.size());
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("maxScore", maxScore);
        model.addAttribute("percentage", questions.isEmpty() ? 0 : (correctCount * 100) / questions.size());
        return "quiz/results";
    }
    @GetMapping("/quiz/create")
    public String createQuizForm(Model model) {
        logger.info("Loading create quiz form");
        model.addAttribute("quiz", new QuizEntity());
        return "quiz/create";
    }
    @PostMapping("/quiz/create")
    public String createQuiz(@Valid @ModelAttribute("quiz") QuizEntity quiz,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        logger.info("Creating new quiz: {}", quiz.getTitle());
        if (bindingResult.hasErrors()) {
            return "quiz/create";
        }
        QuizEntity savedQuiz = quizService.create(quiz);
        redirectAttributes.addFlashAttribute("success", "Quiz created successfully!");
        return "redirect:/quiz/" + savedQuiz.getId() + "/edit";
    }
    @GetMapping("/quiz/{id}/edit")
    public String editQuizForm(@PathVariable Long id, Model model) {
        logger.info("Loading edit form for quiz ID: {}", id);
        Optional<QuizEntity> quiz = quizService.findById(id);
        if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
            model.addAttribute("questions", quiz.get().getQuestions());
            model.addAttribute("newQuestion", new QuestionEntity());
            return "quiz/edit";
        }
        return "redirect:/quizzes?error=Quiz not found";
    }
    @PostMapping("/quiz/{id}/edit")
    public String updateQuiz(@PathVariable Long id,
                             @Valid @ModelAttribute("quiz") QuizEntity quiz,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        logger.info("Updating quiz with ID: {}", id);
        if (bindingResult.hasErrors()) {
            return "quiz/edit";
        }
        try {
            quizService.update(id, quiz);
            redirectAttributes.addFlashAttribute("success", "Quiz updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/quiz/" + id + "/edit";
    }
    @PostMapping("/quiz/{id}/delete")
    public String deleteQuiz(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting quiz with ID: {}", id);
        if (quizService.exists(id)) {
            quizService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Quiz deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Quiz not found!");
        }
        return "redirect:/quizzes";
    }
    @PostMapping("/quiz/{quizId}/question/add")
    public String addQuestion(@PathVariable Long quizId,
                              @Valid @ModelAttribute("newQuestion") QuestionEntity question,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        logger.info("Adding question to quiz ID: {}", quizId);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid question data");
            return "redirect:/quiz/" + quizId + "/edit";
        }
        try {
            quizService.addQuestionToQuiz(quizId, question);
            redirectAttributes.addFlashAttribute("success", "Question added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/quiz/" + quizId + "/edit";
    }
    @GetMapping("/question/{id}/edit")
    public String editQuestionForm(@PathVariable Long id, Model model) {
        logger.info("Loading edit form for question ID: {}", id);
        Optional<QuestionEntity> question = questionService.findById(id);
        if (question.isPresent()) {
            model.addAttribute("question", question.get());
            return "question/edit";
        }
        return "redirect:/quizzes?error=Question not found";
    }
    @PostMapping("/question/{id}/edit")
    public String updateQuestion(@PathVariable Long id,
                                 @Valid @ModelAttribute("question") QuestionEntity question,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        logger.info("Updating question with ID: {}", id);
        if (bindingResult.hasErrors()) {
            return "question/edit";
        }
        try {
            QuestionEntity updated = questionService.update(id, question);
            Long quizId = updated.getQuiz() != null ? updated.getQuiz().getId() : null;
            redirectAttributes.addFlashAttribute("success", "Question updated successfully!");
            if (quizId != null) {
                return "redirect:/quiz/" + quizId + "/edit";
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/quizzes";
    }
    @PostMapping("/question/{id}/delete")
    public String deleteQuestion(@PathVariable Long id,
                                 @RequestParam(required = false) Long quizId,
                                 RedirectAttributes redirectAttributes) {
        logger.info("Deleting question with ID: {}", id);
        if (questionService.exists(id)) {
            questionService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Question deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Question not found!");
        }
        if (quizId != null) {
            return "redirect:/quiz/" + quizId + "/edit";
        }
        return "redirect:/quizzes";
    }
    @GetMapping("/categories")
    public String listCategories(Model model) {
        logger.info("Loading categories");
        List<String> categories = questionService.getAllCategories();
        model.addAttribute("categories", categories);
        return "category/list";
    }
    @GetMapping("/category/{category}")
    public String viewCategory(@PathVariable String category, Model model) {
        logger.info("Viewing questions in category: {}", category);
        List<QuestionEntity> questions = questionService.findByCategory(category);
        model.addAttribute("category", category);
        model.addAttribute("questions", questions);
        return "category/view";
    }
}
