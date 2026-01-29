package Data;

import Entity.*;
import Repository.*;
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
        if (quizRepository.count() > 0) {
            System.out.println("Sample data already exists, skipping initialization.");
            return;
        }
        QuizEntity geographyQuiz = new QuizEntity();
        geographyQuiz.setTitle("World Geography Quiz");
        geographyQuiz.setDescription("Test your knowledge of world capitals and geography!");
        geographyQuiz.setCategory("Geography");

        QuestionEntity q1 = new QuestionEntity(
                "What is the capital of Poland?",
                "A",
                10,
                "Geography",
                "Warsaw", "Krakow", "Gdansk", "Poznan"
        );

        QuestionEntity q2 = new QuestionEntity(
                "What is the capital of Germany?",
                "B",
                10,
                "Geography",
                "Munich", "Berlin", "Hamburg", "Frankfurt"
        );

        QuestionEntity q3 = new QuestionEntity(
                "What is the largest country by area?",
                "C",
                15,
                "Geography",
                "Canada", "China", "Russia", "USA"
        );

        geographyQuiz.addQuestion(q1);
        geographyQuiz.addQuestion(q2);
        geographyQuiz.addQuestion(q3);
        quizRepository.save(geographyQuiz);

        QuizEntity scienceQuiz = new QuizEntity();
        scienceQuiz.setTitle("Basic Science Quiz");
        scienceQuiz.setDescription("Test your basic science knowledge!");
        scienceQuiz.setCategory("Science");

        QuestionEntity s1 = new QuestionEntity(
                "What is the chemical symbol for water?",
                "B",
                10,
                "Science",
                "O2", "H2O", "CO2", "NaCl"
        );

        QuestionEntity s2 = new QuestionEntity(
                "What planet is known as the Red Planet?",
                "A",
                10,
                "Science",
                "Mars", "Venus", "Jupiter", "Saturn"
        );

        QuestionEntity s3 = new QuestionEntity(
                "What is the speed of light approximately?",
                "D",
                20,
                "Science",
                "150,000 km/s", "200,000 km/s", "250,000 km/s", "300,000 km/s"
        );

        scienceQuiz.addQuestion(s1);
        scienceQuiz.addQuestion(s2);
        scienceQuiz.addQuestion(s3);
        quizRepository.save(scienceQuiz);

        QuizEntity programmingQuiz = new QuizEntity();
        programmingQuiz.setTitle("Java Programming Quiz");
        programmingQuiz.setDescription("Test your Java programming knowledge!");
        programmingQuiz.setCategory("Programming");

        QuestionEntity p1 = new QuestionEntity(
                "What is the parent class of all classes in Java?",
                "C",
                10,
                "Programming",
                "Class", "Super", "Object", "Parent"
        );

        QuestionEntity p2 = new QuestionEntity(
                "Which keyword is used to inherit a class in Java?",
                "B",
                10,
                "Programming",
                "implements", "extends", "inherits", "super"
        );

        QuestionEntity p3 = new QuestionEntity(
                "What is the default value of a boolean in Java?",
                "A",
                15,
                "Programming",
                "false", "true", "null", "0"
        );

        programmingQuiz.addQuestion(p1);
        programmingQuiz.addQuestion(p2);
        programmingQuiz.addQuestion(p3);
        quizRepository.save(programmingQuiz);
        System.out.println("\n=== Sample Data Initialized ===");
        System.out.println("Created " + quizRepository.count() + " quizzes");
        System.out.println("Created " + questionRepository.count() + " questions");
        System.out.println("===============================\n");

    }
}
