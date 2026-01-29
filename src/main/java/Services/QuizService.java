package Services;

import Data.DataLoader;
import Entity.QuestionEntity;
import Entity.QuizEntity;
import Repository.QuestionRepository;
import Repository.QuizRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final DataLoader dataLoader;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository, DataLoader dataLoader) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.dataLoader = dataLoader;
    }
    public QuizEntity create(QuizEntity quizEntity) {
        logger.info("Creating quiz: {}", quizEntity.getTitle());
        return quizRepository.save(quizEntity);
    }
    public Optional<QuizEntity> findById(Long id) {
        logger.info("Finding quiz by ID: {}", id);
        return quizRepository.findById(id);
    }
    public List<QuizEntity> findAll() {
        logger.info("Finding all quizzes");
        return quizRepository.findAll();
    }
    public QuizEntity update(Long id, QuizEntity quizEntity) {
        logger.info("Updating quiz with ID: {}", id);
        Optional<QuizEntity> quizOptions = quizRepository.findById(id);
        if (quizOptions.isPresent()) {
            QuizEntity quiz = quizOptions.get();
            quiz.setTitle(quizEntity.getTitle());
            quiz.setDescription(quizEntity.getDescription());
            quiz.setCategory(quizEntity.getCategory());
            return quizRepository.save(quiz);
        } else {
            throw new RuntimeException("Quiz not found with id: " + id);
        }
    }
    public void delete(Long id) {
        logger.info("Deleting quiz with ID: {}", id);
        quizRepository.deleteById(id);
    }
    public boolean exists(Long id) {
        return quizRepository.existsById(id);
    }
    public QuestionEntity addQuestionToQuiz(Long quizId, QuestionEntity question) {
        Optional<QuizEntity> quizOptions = quizRepository.findById(quizId);
        if (quizOptions.isPresent()) {
            QuizEntity quizEntity = quizOptions.get();
            quizEntity.addQuestion(question);
            quizRepository.save(quizEntity);
            return question;
        } else {
            throw new RuntimeException("Quiz not found with id: " + quizId);
        }
    }
    public boolean checkAnswer(Long questionId, String userAnswer) {
        Optional<QuestionEntity> questionOption = questionRepository.findById(questionId);
        if (questionOption.isPresent()) {
            return questionOption.get().isCorrectAnswer(userAnswer);
        }
        throw new RuntimeException("Question not found with id: " + questionId);
    }
    public CompletableFuture<QuizEntity> loadFromExternalSource(String externalId) {
        logger.info("Loading quiz from external source: {}", externalId);
        return dataLoader.loadQuizWithErrorHandling(externalId)
                .thenApply(quiz -> {
                    logger.info("Saving externally loaded quiz: {}", quiz.getTitle());
                    return quizRepository.save(quiz);
                });
    }
    public CompletableFuture<List<QuizEntity>> loadMultipleFromExternal(List<String> externalIds) {
        logger.info("Loading {} quizzes concurrently", externalIds.size());
        return dataLoader.loadMultipleQuizzesAsync(externalIds)
                .thenApply(quizzes -> {
                    logger.info("Saving {} externally loaded quizzes", quizzes.size());
                    return quizRepository.saveAll(quizzes);
                });
    }
}
