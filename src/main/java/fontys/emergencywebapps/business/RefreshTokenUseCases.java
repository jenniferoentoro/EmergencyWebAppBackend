package fontys.emergencywebapps.business;

import fontys.emergencywebapps.persistence.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenUseCases {

    RefreshToken createRefreshToken(String username);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);
}
