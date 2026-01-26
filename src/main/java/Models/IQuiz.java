package Models;

import java.util.List;

public interface IQuiz<T> {
    void addQuestion (Question<T> question);
    boolean removeQuestion (int questionId);
    Question<T> getQuestion (int index);
    List<Question<T>> getAllQuestions ();
    void userAnswers (IAnswer<T> answer);
    int calculateScore ();
    int getTotalScore ();
    int getQuestionCount ();
    String getQuizId ();
    String getTitle ();
    void setTitle (String title);
}