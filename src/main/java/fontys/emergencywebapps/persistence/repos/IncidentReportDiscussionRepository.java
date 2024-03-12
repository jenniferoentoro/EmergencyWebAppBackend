package fontys.emergencywebapps.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import fontys.emergencywebapps.persistence.entities.IncidentReportDiscussion;

public interface IncidentReportDiscussionRepository extends JpaRepository<IncidentReportDiscussion, Long> {
    Iterable<IncidentReportDiscussion> findAllByIncidentReportId(Long id);
}
