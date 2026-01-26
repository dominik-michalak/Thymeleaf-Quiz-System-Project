package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz<T> implements IQuiz<T> {
    private String quizId;
    private String title;
    private List<Question<T>> questions = new ArrayList<>();
    private Map<String, Answer<T>> userAnswers = new HashMap<>();
    private int totalScore;

    public Quiz() {} //for gson to read it
    public Quiz(String quizId, String title, List<Question<T>> questions) {
        this.quizId = quizId;
        this.title = title;
        this.questions = questions;
        this.userAnswers = new HashMap<>();
        this.totalScore = 0;
    }
    public String getQuizId() {
        return quizId;
    }
    public String getTitle() {
        return title;
    }
    public List<Question<T>> getAllQuestions() {
        return questions;
    }

    @Override
    public void userAnswers(IAnswer<T> answer) {
        userAnswers.put(answer.getQuestionId(), (Answer<T>) answer);
    }

    public Map<String, Answer<T>> getUserAnswers() {
        return userAnswers;
    }
    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<Question<T>> getQuestions() { //for whole list of questions
        return questions;
    }
    public void setQuestions(List<Question<T>> questions) {
        this.questions = questions;
    }
    public void setUserAnswers(Map<String, Answer<T>> userAnswers) {
        this.userAnswers = userAnswers;
    }
    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void addUserAnswer(Answer<T> answer) {
        userAnswers.put(answer.getQuestionId(), answer);
    }
    public int calculateScore(){
        int score = 0;
        for(IQuestion<T> question : questions){
            Answer<T> answer = userAnswers.get(question.getId());
            if(answer != null && question.checkAnswer(answer)){
                score += question.getScore();
            }
        }
        this.totalScore = score;
        return score;
    }
    @Override
    public void addQuestion(Question<T> question){
        questions.add((Question<T>) question);
    }

    @Override
    public boolean removeQuestion(int questionId) {
        return questions.removeIf(q -> q.getId().equals(String.valueOf(questionId)));
    }

    @Override
    public Question<T> getQuestion(int index) {
        return questions.get(index);
    }

    public int getMaxPossibleScore(){
        return questions.stream().mapToInt(IQuestion::getScore).sum();
    }
    public int getQuestionCount(){
        return questions.size();
    }
    @Override
    public String toString() {
        return title + " (ID: " + quizId + ")";
    }
}