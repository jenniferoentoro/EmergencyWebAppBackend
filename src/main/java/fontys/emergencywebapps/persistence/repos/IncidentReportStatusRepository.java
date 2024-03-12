package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.IncidentReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentReportStatusRepository extends JpaRepository<IncidentReportStatus, Long> {
    Iterable<IncidentReportStatus> findByIncidentReportId(Long id);
}
