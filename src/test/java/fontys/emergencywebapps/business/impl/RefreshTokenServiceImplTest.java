package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.persistence.entities.RefreshToken;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.RefreshTokenRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {NewsServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RefreshTokenServiceImpl refreshTokenService;

    @Test
    void testCreateRefreshToken_UserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> refreshTokenService.createRefreshToken(username));
    }

    @Test
    void testFindByToken_Success() {
        String token = "testToken";
        RefreshToken mockRefreshToken = RefreshToken.builder().token(token).expiryDate(Instant.now().plusMillis(60000)).build();
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(mockRefreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertTrue(result.isPresent());
        assertEquals(mockRefreshToken, result.get());
    }

    @Test
    void testFindByToken_NotFound() {
        // Test when the refresh token is not found
        String token = "nonExistingToken";
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertTrue(result.isEmpty());
    }

    @Test
    void testVerifyExpiration_NotExpired() {
        RefreshToken mockToken = RefreshToken.builder().expiryDate(Instant.now().plusMillis(600000)).build();

        RefreshToken result = refreshTokenService.verifyExpiration(mockToken);

        assertEquals(mockToken, result);
    }

    @Test
    void testVerifyExpiration_Expired() {
        RefreshToken mockToken = RefreshToken.builder().expiryDate(Instant.now().minusMillis(600000)).build();
        assertThrows(CustomException.class, () -> refreshTokenService.verifyExpiration(mockToken));
    }


    @Test
    void testCreateRefreshToken_UserFound_ExistingTokenFound() {
        String username = "existingUser";
        User mockUser = User.builder().username(username).build();
        RefreshToken existingToken = RefreshToken.builder()
                .user(mockUser)
                .token("existingToken")
                .expiryDate(Instant.now().plusMillis(600000))
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(refreshTokenRepository.findByUser(mockUser)).thenReturn(Optional.of(existingToken));
        when(refreshTokenRepository.save(existingToken)).thenReturn(existingToken);

        RefreshToken result = refreshTokenService.createRefreshToken(username);

        assertNotNull(result);
        assertEquals(existingToken, result);
    }

    @Test
    void testCreateRefreshToken_UserFound_NoExistingToken() {
        String username = "newUser";
        User mockUser = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(refreshTokenRepository.findByUser(mockUser)).thenReturn(Optional.empty());

        when(refreshTokenRepository.save(Mockito.any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));  // Return the saved refresh token

        RefreshToken result = refreshTokenService.createRefreshToken(username);

        assertNotNull(result);
        assertEquals(mockUser, result.getUser());
        assertNotNull(result.getToken());
        assertNotNull(result.getExpiryDate());
    }

}