package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;

import java.util.List;


public interface NewsRepository extends JpaRepository<News, Long> {

    Iterable<News> findByTitleContaining(String title);



    List<News> findByIncidentCategory(IncidentCategory incidentCategory);

}
