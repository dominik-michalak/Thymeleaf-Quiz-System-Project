package Data;

import Entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
@Service
public class DataLoader {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    private final ExecutorService executorService;
    private final AtomicInteger loadedQuizCount = new AtomicInteger(0);
    public DataLoader() {
        int processors = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(processors);
        logger.info("Data loader has been initialized with {} threads", processors);
    }
    public Future<QuizEntity> loadQuizAsync(String quizId){
        Callable<QuizEntity> loadTask = () -> {
            logger.info("Thread {} is loading quiz {}", Thread.currentThread().getName(), quizId);
            Thread.sleep(1000);
            QuizEntity quiz = createQuiz(quizId);
            loadedQuizCount.incrementAndGet();
            logger.info("Quiz {} has been loaded", quizId);
            return quiz;
        };
        return executorService.submit(loadTask);
    }
    public CompletableFuture<List<QuizEntity>> loadMultipleQuizzesAsync(List<String> quizIds) {
        logger.info("Starting concurrent load of {} quizzes", quizIds.size());
        List<CompletableFuture<QuizEntity>> futures = new ArrayList<>();
        for (String quizId : quizIds) {
            CompletableFuture<QuizEntity> future = CompletableFuture.supplyAsync(() -> {
                try {
                    logger.info("Loading quiz: {} on thread: {}",
                            quizId, Thread.currentThread().getName());
                    Thread.sleep(500 + (long)(Math.random() * 1000));
                    QuizEntity quiz = createQuiz(quizId);
                    loadedQuizCount.incrementAndGet();
                    return quiz;
                } catch (InterruptedException e) {
                    logger.error("Error loading quiz: {}", quizId, e);
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Failed to load quiz: " + quizId, e);
                }
            }, executorService);
            futures.add(future);
        }
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        return allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .toList()
        );
    }
    public CompletableFuture<QuizEntity> loadQuizWithTimeout(String quizId, long timeoutSeconds) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000);
                        return createQuiz(quizId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted", e);
                    }
                }, executorService)
                .completeOnTimeout(
                        createDefaultQuiz(quizId),
                        timeoutSeconds,
                        TimeUnit.SECONDS
                );
    }
    public CompletableFuture<QuizEntity> loadQuizWithErrorHandling(String quizId) {
        return CompletableFuture.supplyAsync(() -> {
                    if (Math.random() < 0.3) {
                        throw new RuntimeException("Simulated error loading quiz: " + quizId);
                    }
                    return createQuiz(quizId);
                }, executorService)
                .exceptionally(throwable -> {
                    logger.error("Error loading quiz, using fallback", throwable);
                    return createDefaultQuiz(quizId);
                });
    }
    public CompletableFuture<String> loadAndProcessQuiz(String quizId) {
        return CompletableFuture.supplyAsync(() -> {
                    logger.info("Step 1: Loading quiz {}", quizId);
                    return createQuiz(quizId);
                }, executorService)
                .thenApply(quiz -> {
                    logger.info("Step 2: Processing quiz {}", quiz.getTitle());
                    return "Processed: " + quiz.getTitle() +
                            " with " + quiz.getQuestions().size() + " questions";
                });
    }
    public int getLoadedQuizCount() {
        return loadedQuizCount.get();
    }
    public void resetCounter() {
        loadedQuizCount.set(0);
    }
    public void shutdown() {
        logger.info("Shutting down executor service");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    private QuizEntity createQuiz(String quizId) {
        QuizEntity quiz = new QuizEntity();
        quiz.setTitle("Quiz " + quizId);
        quiz.setDescription("Loaded from external source - ID: " + quizId);

        for (int i = 1; i <= 3; i++) {
            QuestionEntity question = new QuestionEntity();
            question.setQuestionText("Question " + i + " from quiz " + quizId);
            question.setCorrectAnswer("Answer " + i);
            question.setScore(10);
            question.setCategory("External Load");

            quiz.addQuestion(question);
        }
        return quiz;
    }
    private QuizEntity createDefaultQuiz(String quizId) {
        QuizEntity quiz = new QuizEntity();
        quiz.setTitle("Default Quiz " + quizId);
        quiz.setDescription("Fallback quiz due to loading error");
        return quiz;
    }
}
