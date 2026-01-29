package Data;

import Entity.QuestionEntity;
import Entity.QuizEntity;
import Repository.QuestionRepository;
import Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleData implements CommandLineRunner {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public SampleData(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        questionRepository.deleteAll();
        quizRepository.deleteAll();

        QuizEntity newQuiz1 = new QuizEntity("Trivia Quiz", "A simple trivia quiz");
        QuestionEntity question1 = new QuestionEntity(
                "Trivia01",
                "What is capital of Poland?",
                "Warsaw",
                10,
                "Trivia",
                "Warsaw",
                "Krakow",
                "Gdansk",
                "Wroclaw"
        );
        QuestionEntity question2 = new QuestionEntity(
                "Trivia02",
                "What is capital of Germany?",
                "Berlin",
                10,
                "Trivia",
                "Berlin",
                "Munich",
                "Frankfurt",
                "Hamburg"
        );
        newQuiz1.addQuestion(question1);
        newQuiz1.addQuestion(question2);

        quizRepository.save(newQuiz1);

        System.out.println("\n=== Sample Data Initialized ===");
        System.out.println("Created " + quizRepository.count() + " quizzes");
        System.out.println("Created " + questionRepository.count() + " questions");
        System.out.println("===============================\n");

    }
}
