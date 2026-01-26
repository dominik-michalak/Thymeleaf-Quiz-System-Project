package Models;

public interface IAnswer<T> {
    String getQuestionId();
    T getAnswerText();
    void setAnswerText(T answerText);
}