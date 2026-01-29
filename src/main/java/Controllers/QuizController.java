package Controllers;


import Entity.*;
import Services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<QuizEntity> createQuiz(@RequestParam String title, @RequestParam(required = false) String description) {
        QuizEntity quiz = quizService.createQuiz(title, description);
        return new ResponseEntity<>(quiz, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuizEntity>> getAllQuizzes() {
        List<QuizEntity> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizEntity> getQuizById(@PathVariable String id) {
        Optional<QuizEntity> quiz = quizService.getQuizById(id);
        return quiz.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizEntity> updateQuiz(@PathVariable String id, @RequestParam String title, @RequestParam(required = false) String description) {
        try {
            QuizEntity updatedQuiz = quizService.updateQuiz(id, title, description);
            return ResponseEntity.ok(updatedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<QuestionEntity> addQuestionToQuiz(@PathVariable String quizId, @RequestBody QuestionEntity question) {
        try {
            QuestionEntity addedQuestion = quizService.addQuestionToQuiz(quizId, question);
            return new ResponseEntity<>(addedQuestion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuizEntity>> searchQuizzes(@RequestParam String title) {
        List<QuizEntity> quizzes = quizService.searchQuizzesByTitle(title);
        return ResponseEntity.ok(quizzes);
    }
}
