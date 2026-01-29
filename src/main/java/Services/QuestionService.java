package Services;

import Entity.QuestionEntity;
import Repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    public QuestionEntity create(QuestionEntity entity) {
        logger.info("Creating question: {}", entity.getQuestionText());
        return questionRepository.save(entity);
    }
    public Optional<QuestionEntity> findById(Long id) {
        logger.info("Finding question by ID: {}", id);
        return questionRepository.findById(id);
    }
    public List<QuestionEntity> findAll() {
        logger.info("Finding all questions");
        return questionRepository.findAll();
    }
    public QuestionEntity update(Long id, QuestionEntity entity) {
        logger.info("Updating question with ID: {}", id);
        Optional<QuestionEntity> questionOption = questionRepository.findById(id);
        if (questionOption.isPresent()) {
            QuestionEntity question = questionOption.get();
            question.setQuestionText(entity.getQuestionText());
            question.setCorrectAnswer(entity.getCorrectAnswer());
            question.setScore(entity.getScore());
            question.setCategory(entity.getCategory());
            question.setOptionA(entity.getOptionA());
            question.setOptionB(entity.getOptionB());
            question.setOptionC(entity.getOptionC());
            question.setOptionD(entity.getOptionD());
            return questionRepository.save(question);
        } else {
            throw new RuntimeException("Question not found with id: " + id);
        }
    }
    public void delete(Long id) {
        logger.info("Deleting question with ID: {}", id);
        questionRepository.deleteById(id);
    }
    public boolean exists(Long id) {
        return questionRepository.existsById(id);
    }
    public List<QuestionEntity> findByCategory(String category) {
        return questionRepository.findByCategory(category);
    }
    public List<QuestionEntity> findByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }
    public List<String> getAllCategories() {
        logger.info("Getting all distinct categories");
        return questionRepository.findDistinctCategories();
    }
    public long countByCategory(String category) {
        return questionRepository.countByCategory(category);
    }
    public boolean checkAnswer(Long questionId, String userAnswer) {
        logger.info("Checking answer for question ID: {}", questionId);
        Optional<QuestionEntity> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isPresent()) {
            return questionOpt.get().isCorrectAnswer(userAnswer);
        }
        throw new RuntimeException("Question not found with id: " + questionId);
    }
}
