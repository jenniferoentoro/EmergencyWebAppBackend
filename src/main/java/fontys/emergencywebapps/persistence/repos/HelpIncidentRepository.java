package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.HelpIncident;
import fontys.emergencywebapps.persistence.entities.StatusHelpIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HelpIncidentRepository extends JpaRepository<HelpIncident, Long> {
    Optional<HelpIncident> findByChatConnectionIdAndStatusHelpIncident(Long id, StatusHelpIncident statusHelpIncident);

    Optional<HelpIncident> findByHelperIdAndStatusHelpIncident(Long id, StatusHelpIncident statusHelpIncident);

    @Query("SELECT h FROM HelpIncident h JOIN h.chatConnection cc WHERE cc.user.id = :userId AND h.statusHelpIncident = :status AND h.helper.id IS NOT NULL")
    Optional<HelpIncident> findByUserIdAndStatusAndHelperNotNull(@Param("userId") Long userId, @Param("status") StatusHelpIncident status);
}
