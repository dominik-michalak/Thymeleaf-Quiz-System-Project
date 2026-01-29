package Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 1000)
    private String questionText;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String correctAnswer;

    @NotNull
    @Column(nullable = false)
    private Integer score;

    @Column(length =  100)
    private String category;

    @Column(length = 200)
    private String optionA;

    @Column(length = 200)
    private String optionB;

    @Column(length = 200)
    private String optionC;

    @Column(length = 200)
    private String optionD;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    @JsonIgnore
    private QuizEntity quiz;

    public QuestionEntity() {}

    public QuestionEntity(String questionText, String correctAnswer, Integer score, String category) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.score = score;
        this.category = category;
    }

    public QuestionEntity(String questionText, String correctAnswer, Integer score, String category,
                          String optionA, String optionB, String optionC, String optionD) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.score = score;
        this.category = category;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public QuizEntity getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizEntity quiz) {
        this.quiz = quiz;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "id='" + id + '\'' +
                ", questionText='" + questionText + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", score=" + score +
                ", category='" + category + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ". quiz=" + (quiz != null ? quiz.getId() : "null") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionEntity that = (QuestionEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean isCorrectAnswer(String userAnswer) {
        if (userAnswer == null || correctAnswer == null) {
            return false;
        }
        return correctAnswer.equalsIgnoreCase(userAnswer.trim());
    }

    public List<String> getAllOptions() {
        List<String> options = new ArrayList<>();
        if (optionA != null) options.add(optionA);
        if (optionB != null) options.add(optionB);
        if (optionC != null) options.add(optionC);
        if (optionD != null) options.add(optionD);
        return options;
    }
}
