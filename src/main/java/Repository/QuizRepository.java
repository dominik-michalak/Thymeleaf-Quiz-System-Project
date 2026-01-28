package Repository;


import Entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Integer> {
    Optional<QuizEntity> findByTitle(String title);
    List<QuizEntity> findByTitleContaining(String category);
    List<QuizEntity> findByCategory(String category);
}
