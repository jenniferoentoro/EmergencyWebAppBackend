package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.NewsBreaking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsBreakingRepository extends JpaRepository<NewsBreaking, Long> {
    Optional<NewsBreaking> findByNewsId(Long idNews);

}
