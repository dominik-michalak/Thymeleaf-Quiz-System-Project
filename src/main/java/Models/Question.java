package Models;
import java.util.HashMap;
import java.util.Map;

public class Question<T> implements IQuestion<T> {
    private String id;
    private String question;
    private T correctAnswer;
    private int score;
    private String category;
    private Map<Character, String> answerOptions = new HashMap<>();

    public Question() {} // for gson to serialize and deserialize

    public Question(String id, String questionText, String correctAns, int points, String category, String answerA, String answerB, String answerC, String answerD) {
        this.id = id;
        this.question = questionText;
        this.correctAnswer = (T) correctAns;
        this.score = points;
        this.category = category;
        this.answerOptions = new HashMap<>();
        this.answerOptions.put('A', answerA);
        this.answerOptions.put('B', answerB);
        this.answerOptions.put('C', answerC);
        this.answerOptions.put('D', answerD);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public T getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(T correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setAnswerOptions(Map<Character, String> answerOptions) {
        this.answerOptions = answerOptions;
    }
    public String getQuestion() {
        return question;
    }

    public T getCorrectAns() {
        return correctAnswer;
    }

    public int getScore() {

        return score;
    }
    public void setScore(int var1) {

        this.score = var1;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {

        this.category = category;
    }
    public Map<Character, String> getAnswerOptions() {
        return answerOptions; }

    public void displayQuestion() {
        System.out.println("Q: " + this.question);
        if (answerOptions != null) {
            for (Map.Entry<Character, String> entry : answerOptions.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
    public boolean checkAnswer(IAnswer<T> answer) {
        if (answer == null || answer.getAnswerText() == null || this.correctAnswer == null) {
            return false;
        }
        if (correctAnswer instanceof String) {
            String userAns = String.valueOf(answer.getAnswerText()).trim();
            String correctAns = String.valueOf(this.correctAnswer).trim();
            return userAns.equalsIgnoreCase(correctAns);
        } else if (correctAnswer instanceof Integer) {
            return correctAnswer.equals(answer.getAnswerText());
        } else if (correctAnswer instanceof Boolean) {
            return correctAnswer.equals(answer.getAnswerText());
        }
        return correctAnswer.equals(answer.getAnswerText());
    }
}