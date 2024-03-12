package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.entities.VerificationTokenOtp;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import fontys.emergencywebapps.persistence.repos.VerificationTokenOtpRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserServiceImpl.class})
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenOtpRepository verificationTokenOtpRepository;

    @Mock
    private AccessTokenDecoder jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUser_Success() {
        String username = "testUser";
        User mockUser = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        User result = userService.getUser(username);

        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    @Test
    void testGetUser_UserFound_ReturnsUser() {
        String username = "testUser";
        User mockUser = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        User result = userService.getUser(username);

        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    @Test
    void testGetUser_UserNotFound_ThrowsException() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.getUser(username));
    }
    @Test
    void testGetUsers_Success() {
        List<User> mockUsers = Arrays.asList(new User(), new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        Iterable<User> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(mockUsers, result);
    }

    @Test
    void testGetUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Iterable<User> result = userService.getUsers();

        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void testCreateVerificationToken_Success() {
        User mockUser = new User();
        String verificationToken = "mockVerificationToken";

        userService.createVerificationToken(mockUser, verificationToken);

        verify(verificationTokenOtpRepository, times(1)).save(any(VerificationTokenOtp.class));
    }

//    @Test
//    void testValidateVerificationToken_Success() {
//        String mockToken = "validToken";
//        VerificationTokenOtp mockOtp = new VerificationTokenOtp(mockToken, new User());
//        when(verificationTokenOtpRepository.findByOtp(mockToken)).thenReturn(mockOtp);
//        when(jwtService.decode(mockToken)).thenReturn(new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER")));
//
//        // Act
//        String result = userService.validateVerificationToken(mockToken);
//
//        // Assert
//        verify(verificationTokenOtpRepository, times(1)).delete(any(VerificationTokenOtp.class));
//        verify(userRepository, times(1)).save(any(User.class));
//        assertEquals("Email verified successfully. Now you can login to your account.", result);
//    }

    @Test
    void testValidateVerificationToken_InvalidToken() {
        String invalidToken = "invalidToken";
        when(verificationTokenOtpRepository.findByOtp(invalidToken)).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.validateVerificationToken(invalidToken));
        verify(verificationTokenOtpRepository, times(1)).findByOtp(invalidToken);
    }

    @Test
    void testValidateVerificationToken_ExpiredToken() {
        String expiredToken = "expiredToken";
        VerificationTokenOtp expiredOtp = new VerificationTokenOtp(expiredToken, new User());
        expiredOtp.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
        when(verificationTokenOtpRepository.findByOtp(expiredToken)).thenReturn(expiredOtp);

        assertThrows(CustomException.class, () -> userService.validateVerificationToken(expiredToken));
        verify(verificationTokenOtpRepository, times(1)).findByOtp(expiredToken);
        verify(verificationTokenOtpRepository, times(1)).delete(expiredOtp);
    }

    @Test
    void testGetUserFromEmail_Success() {
        String userEmail = "test@example.com";
        User mockUser = new User(/* Set user properties */);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserFromEmail(userEmail);

        assertEquals(mockUser, result);
    }

    @Test
    void testGetUserFromEmail_UserNotFound() {
        String userEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.getUserFromEmail(userEmail));
    }

    @Test
    void testGetMe_Success() {
        String mockToken = "validToken";
        AccessTokenImpl mockClaims = new AccessTokenImpl(mockToken, 1L, Collections.singleton("USER"));
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + mockToken);
        when(jwtService.decode(mockToken)).thenReturn(mockClaims);

        User mockUser = new User(/* Set user properties */);
        when(userRepository.findById(mockClaims.getUserId())).thenReturn(Optional.of(mockUser));

        User result = userService.getMe(mockRequest);

        assertEquals(mockUser, result);
    }

    @Test
    void testGetMe_UserNotFound() {
        String mockToken = "validToken";
        AccessTokenImpl mockClaims = new AccessTokenImpl(mockToken, 1L, Collections.singleton("USER"));
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + mockToken);
        when(jwtService.decode(mockToken)).thenReturn(mockClaims);

        when(userRepository.findById(mockClaims.getUserId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.getMe(mockRequest));
    }

    @Test
    void testGetChatAdmins_Success() {
        List<User> mockUsers = Arrays.asList(new User(), new User(), new User());
        when(userRepository.findUserIdsAndUsernamesWithOngoingChatConnection()).thenReturn(mockUsers);

        List<User> result = userService.getChatAdmins();

        assertEquals(mockUsers, result);
    }

    @Test
    void testGetChatAdmins_UsersNotFound() {
        when(userRepository.findUserIdsAndUsernamesWithOngoingChatConnection()).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.getChatAdmins());
    }



}