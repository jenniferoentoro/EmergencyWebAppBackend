package fontys.emergencywebapps.persistence.repos;
import org.springframework.data.jpa.repository.JpaRepository;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;

import java.util.List;

public interface IncidentCategoryRepository extends JpaRepository<IncidentCategory, Long> {
    IncidentCategory findByName(String name);

    void deleteAllByIdIn(List<Long> ids);
}
