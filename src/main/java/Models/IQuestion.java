package Models;


public interface IQuestion<T> {
    String getId();
    String getQuestion();
    T getCorrectAns();
    int getScore();
    void setScore(int score);
    String getCategory();
    void setCategory(String category);
    void displayQuestion();
    boolean checkAnswer(IAnswer<T> answer);
}