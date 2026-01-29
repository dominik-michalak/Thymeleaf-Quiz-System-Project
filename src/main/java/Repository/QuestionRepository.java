package Repository;

import Entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    List<QuestionEntity> findByCategory(String category);
    List<QuestionEntity> findByQuizId(Long quizId);
    long countByCategory(String category);
    long countByQuizId(Long quizId);
    @Query("SELECT SUM(q.score) FROM QuestionEntity q WHERE q.quiz.id = :quizId")
    Integer sumScoresByQuizId(@Param("quizId") Long quizId);
    @Query("SELECT DISTINCT q.category FROM QuestionEntity q WHERE q.category IS NOT NULL")
    List<String> findDistinctCategories();
}
