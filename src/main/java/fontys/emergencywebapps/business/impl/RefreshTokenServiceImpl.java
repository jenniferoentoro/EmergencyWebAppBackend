package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.RefreshTokenUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.persistence.entities.RefreshToken;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.RefreshTokenRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Data
public class RefreshTokenServiceImpl implements RefreshTokenUseCases {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;


    @Override
    public RefreshToken createRefreshToken(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<RefreshToken> existingTokenOptional = refreshTokenRepository.findByUser(user);

            if (existingTokenOptional.isPresent()) {
                RefreshToken existingToken = existingTokenOptional.get();
                existingToken.setExpiryDate(Instant.now().plusMillis(600000));
                return refreshTokenRepository.save(existingToken);
            } else {
                RefreshToken refreshToken = RefreshToken.builder()
                        .user(user)
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Instant.now().plusMillis(600000))
                        .build();

                return refreshTokenRepository.save(refreshToken);
            }
        } else {
            throw new CustomException("User not found");
        }
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new CustomException("Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

}

