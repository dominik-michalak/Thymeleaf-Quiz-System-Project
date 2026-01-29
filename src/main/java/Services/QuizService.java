package Services;


import Entity.QuestionEntity;
import Entity.QuizEntity;
import Repository.QuestionRepository;
import Repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }
    public QuizEntity createQuiz(String title, String description) {
        QuizEntity quiz = new QuizEntity(title, description);
        return quizRepository.save(quiz);
    }

    public List<QuizEntity> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<QuizEntity> getQuizById(String id) {
        return quizRepository.findById(id);
    }

    public QuizEntity updateQuiz(String id, String title, String description) {
        Optional<QuizEntity> quizOptions = quizRepository.findById(id);
        if(quizOptions.isPresent()) {
            QuizEntity quiz = quizOptions.get();
            quiz.setTitle(title);
            quiz.setDescription(description);
            return quizRepository.save(quiz);
        } else {
            throw new RuntimeException("Quiz not found");
        }
    }

    public void deleteQuiz(String id) {
        quizRepository.deleteById(id);
    }

    public QuestionEntity addQuestionToQuiz(String quizId, QuestionEntity question) {
        Optional<QuizEntity> quizOptions = quizRepository.findById(quizId);
        if(quizOptions.isPresent()) {
            QuizEntity quizEntity = quizOptions.get();
            quizEntity.addQuestion(question);
            quizRepository.save(quizEntity);
            return question;
        } else {
            throw new RuntimeException("Quiz not found");
        }
    }

    public List<QuestionEntity> getQuestionsByQuizId(String quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public List<QuizEntity> searchQuizzesByTitle(String keyword){
        return quizRepository.findByTitleContaining(keyword);
    }

    public boolean checkAnswer(String questionId, String userAnswer) {
        Optional<QuestionEntity> questionOption = questionRepository.findById(questionId);
        if(questionOption.isPresent()) {
            QuestionEntity question = questionOption.get();
            return question.getCorrectAnswer().equalsIgnoreCase(userAnswer);
        }
        return false;
    }
}
