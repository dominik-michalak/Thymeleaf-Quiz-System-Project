package Services;

import Entity.QuestionEntity;
import Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionEntity createQuestion(String questionText, String correctAnswer, Integer score, String category) {
        QuestionEntity question = new QuestionEntity(questionText, correctAnswer, score, category);
        return questionRepository.save(question);
    }

    public List<QuestionEntity> getAllQuestions(){
        return questionRepository.findAll();
    }

    public Optional<QuestionEntity> getQuestionById(String id) {
        return questionRepository.findById(id);
    }

    public List<QuestionEntity> getQuestionsByCategory(String category) {
        return questionRepository.findByCategory(category);
    }

    public QuestionEntity updateQuestion(String id, String questionText, String correctAnswer, Integer score, String category) {
        Optional<QuestionEntity> questionOption = questionRepository.findById(id);
        if(questionOption.isPresent()) {
            QuestionEntity question = questionOption.get();
            question.setQuestionText(questionText);
            question.setCorrectAnswer(correctAnswer);
            question.setScore(score);
            question.setCategory(category);
            return questionRepository.save(question);
        } else {
            throw new RuntimeException("Question not found");
        }
    }

    public void deleteQuestion(String id){
        questionRepository.deleteById(id);
    }

}
