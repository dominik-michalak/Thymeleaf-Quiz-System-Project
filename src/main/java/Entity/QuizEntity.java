package Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column (nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String category;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions = new ArrayList<>();

    public QuizEntity() {}

    public QuizEntity(Long id, String title, String description, List<QuestionEntity> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = questions != null ? questions : new ArrayList<>();
    }

    public void addQuestion(QuestionEntity question) {
        questions.add(question);
        question.setQuiz(this);
    }

    public void removeQuestion(QuestionEntity question) {
        questions.remove(question);
        question.setQuiz(null);
    }

    public int calculateTotalScore() {
        return questions.stream().mapToInt(QuestionEntity::getScore).sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "QuizEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", questionCount=" + questions.size() +
                '}';
    }

}
