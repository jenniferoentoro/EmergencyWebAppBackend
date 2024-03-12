package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    Iterable<Tutorial> findAllByTitleContaining(String title);
}
