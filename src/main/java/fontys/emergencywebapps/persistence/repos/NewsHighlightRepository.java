package fontys.emergencywebapps.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import fontys.emergencywebapps.persistence.entities.NewsHighlight;

public interface NewsHighlightRepository extends JpaRepository<NewsHighlight, Long> {
    NewsHighlight findByNewsId(Long idNews);
}
