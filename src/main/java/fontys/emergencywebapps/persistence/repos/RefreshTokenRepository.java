package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.RefreshToken;
import fontys.emergencywebapps.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByUser(User user);

   Optional<RefreshToken> findByToken(String token);
}
