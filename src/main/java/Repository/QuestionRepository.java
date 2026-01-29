package Repository;

import Entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {

    List<QuestionEntity> findByCategory(String category);
    List<QuestionEntity> findByQuizId(String quizId);


    Optional<QuestionEntity> findById(String id);

    void deleteById(String id);
}
