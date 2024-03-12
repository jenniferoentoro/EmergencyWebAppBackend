package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.HelperInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelperInformationRepository extends JpaRepository<HelperInformation, Long> {
    Optional<HelperInformation> findByUserId(Long id);

}
