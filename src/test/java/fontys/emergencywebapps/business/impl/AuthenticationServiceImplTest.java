package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.configurations.security.token.AccessTokenEncoder;
import fontys.emergencywebapps.controllers.dto.*;
import fontys.emergencywebapps.persistence.entities.UserRoles;
import fontys.emergencywebapps.persistence.entities.VerificationTokenOtp;
import fontys.emergencywebapps.persistence.repos.VerificationTokenOtpRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.persistence.entities.Role;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ContextConfiguration(classes = {AuthenticationServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenOtpRepository verificationTokenOtpRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    ModelMapper modelMapper;

    @Test
    void testRegister_UserAlreadyExists_ShouldThrowCustomException() {
        when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new User()));
        UserRegisterRequest request = UserRegisterRequest.builder().username("existingUsername").build();
        assertThrows(CustomException.class, () -> authenticationService.register(request));
        verify(userRepository).findByUsername("existingUsername");
    }

    @Test
    void testRegister_IdentityCardNumberAlreadyExists_ShouldThrowCustomException() {
        when(userRepository.findByIdentityCardNumber("existingIdCard")).thenReturn(Optional.of(new User()));
        UserRegisterRequest request = UserRegisterRequest.builder().identityCardNumber("existingIdCard").build();
        request.setIdentityCardNumber("existingIdCard");
        assertThrows(CustomException.class, () -> authenticationService.register(request));
        verify(userRepository).findByIdentityCardNumber("existingIdCard");
    }

    @Test
    void testRegister_EmailAlreadyExists_ShouldThrowCustomException() {
        when(userRepository.findByEmail("existingEmail@example.com")).thenReturn(Optional.of(new User()));
        UserRegisterRequest request = UserRegisterRequest.builder().email("existingEmail@example.com").build();
        assertThrows(CustomException.class, () -> authenticationService.register(request));
        verify(userRepository).findByEmail("existingEmail@example.com");
    }

    @Test
    void testRegister_PhoneNumberAlreadyExists_ShouldThrowCustomException() {
        when(userRepository.findByPhoneNumber("031234567")).thenReturn(Optional.of(new User()));
        UserRegisterRequest request = UserRegisterRequest.builder().phoneNumber("031234567").build();
        request.setPhoneNumber("031234567");
        assertThrows(CustomException.class, () -> authenticationService.register(request));
        verify(userRepository).findByPhoneNumber("031234567");
    }

    @Test
    void testRegister_InvalidPassword_ShouldThrowCustomException() {
        UserRegisterRequest request = UserRegisterRequest.builder().password("short").build();
        assertThrows(CustomException.class, () -> authenticationService.register(request));
    }

    @Test
    void testRegister_SuccessfulRegistration_ShouldReturnToken() {

        UserRegisterRequest request = UserRegisterRequest.builder()
                .firstName("First")
                .lastName("Last")
                .birthDate(new Date())
                .address("Address")
                .identityCardNumber("newIdCard")
                .email("newEmail@example.com")
                .username("newUsername")
                .phoneNumber("newPhoneNumber")
                .password("validPassword123")
                .roles(Set.of(String.valueOf(Role.USER)))
                .build();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByIdentityCardNumber(request.getIdentityCardNumber())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.encode(any(AccessTokenImpl.class))).thenReturn("mockedJWTToken");

        UserAuthenticationResponse token = authenticationService.register(request);
        assertNotNull(token);
        assertEquals(UserAuthenticationResponse.builder().token("mockedJWTToken").build(), token);
        verify(userRepository).save(any(User.class));
        verify(jwtService).encode(any(AccessTokenImpl.class));
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByIdentityCardNumber(request.getIdentityCardNumber());
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).findByPhoneNumber(request.getPhoneNumber());
    }

    @Test
    void testAuthenticate_SuccessfulAuthentication_ShouldReturnToken() {
        UserLoginRequest request = UserLoginRequest.builder()
                .username("existingUsername")
                .password("validPassword123")
                .build();

        User user = User.builder()
                .username(request.getUsername())
                .password("hashedPassword")
                .emailVerifiedAt(new Date())
                .roles(Collections.singleton(UserRoles.builder().role(Role.USER).build()))
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.encode(any(AccessTokenImpl.class))).thenReturn("mockedJWTToken");

        UserAuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("mockedJWTToken", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername(request.getUsername());
        verify(jwtService).encode(any(AccessTokenImpl.class));
    }

    @Test
    void testAuthenticate_InvalidCredentials_ShouldThrowCustomException() {
        UserLoginRequest request = UserLoginRequest.builder()
                .username("nonExistingUsername")
                .password("invalidPassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new CustomException("Authentication failed"));

        assertThrows(CustomException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticate_UnverifiedEmail_ShouldThrowCustomException() {
        UserLoginRequest request = UserLoginRequest.builder()
                .username("existingUsername")
                .password("validPassword123")
                .build();

        User user = User.builder()
                .username(request.getUsername())
                .password("hashedPassword")
                .emailVerifiedAt(null)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        assertThrows(CustomException.class, () -> authenticationService.authenticate(request));
    }


    @Test
    void testRegisterUser_SuccessfulRegistration_ShouldReturnToken() {
        UserRegisterForUserRequest request = UserRegisterForUserRequest.builder()
                .username("newUsername")
                .identityCardNumber("newIdCard")
                .email("newEmail@example.com")
                .phoneNumber("newPhoneNumber")
                .password("validPassword123")
                .firstName("First")
                .lastName("Last")
                .address("Address")
                .birthDate(new Date())
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByIdentityCardNumber(request.getIdentityCardNumber())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.encode(any(AccessTokenImpl.class))).thenReturn("mockedJWTToken");

        UserAuthenticationResponse response = authenticationService.registerUser(request);

        assertNotNull(response);
        assertEquals("mockedJWTToken", response.getToken());
        verify(userRepository).save(any(User.class));
        verify(jwtService).encode(any(AccessTokenImpl.class));
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByIdentityCardNumber(request.getIdentityCardNumber());
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).findByPhoneNumber(request.getPhoneNumber());
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists_ShouldThrowCustomException() {
        UserRegisterForUserRequest request = UserRegisterForUserRequest.builder()
                .username("existingUsername")
                .build();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(CustomException.class, () -> authenticationService.registerUser(request));
    }

    @Test
    void testRegisterUser_IdentityCardNumberAlreadyExists_ShouldThrowCustomException() {
        UserRegisterForUserRequest request = UserRegisterForUserRequest.builder()
                .identityCardNumber("existingIdCard")
                .build();
        when(userRepository.findByIdentityCardNumber(request.getIdentityCardNumber())).thenReturn(Optional.of(new User()));

        assertThrows(CustomException.class, () -> authenticationService.registerUser(request));
    }
    @Test
    void testRegisterUser_EmailAlreadyExists_ShouldThrowCustomException() {
        UserRegisterForUserRequest request = UserRegisterForUserRequest.builder()
                .email("existingEmail@example.com")
                .build();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(CustomException.class, () -> authenticationService.registerUser(request));
    }

    @Test
    void testRegisterUser_PhoneNumberAlreadyExists_ShouldThrowCustomException() {
        UserRegisterForUserRequest request = UserRegisterForUserRequest.builder()
                .phoneNumber("existingPhoneNumber")
                .build();
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.of(new User()));

        assertThrows(CustomException.class, () -> authenticationService.registerUser(request));
    }

    @Test
    void testRegisterUser_InvalidPassword_ShouldThrowCustomException() {
        UserRegisterForUserRequest request = UserRegisterForUserRequest.builder().password("short").build();
        assertThrows(CustomException.class, () -> authenticationService.registerUser(request));
    }

    @Test
    void testUserAskVerificationToken_EmailNotFound_ShouldThrowCustomException() {
        UserVerificationRequest request = UserVerificationRequest.builder()
                .email("nonExistentEmail@example.com")
                .build();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> authenticationService.userAskVerificationToken(request));
    }

    @Test
    void testUserAskVerificationToken_SuccessfulTokenGeneration() {
        UserVerificationRequest request = UserVerificationRequest.builder()
                .email("existingEmail@example.com")
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .emailVerifiedAt(null)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(verificationTokenOtpRepository.findByUserEmail(request.getEmail())).thenReturn(null);

        User result = authenticationService.userAskVerificationToken(request);

        assertNotNull(result);
        verify(verificationTokenOtpRepository, never()).delete(any(VerificationTokenOtp.class));
    }


    @Test
    void testUserAskVerificationToken_EmailAlreadyVerified_ShouldThrowCustomException() {
        UserVerificationRequest request = UserVerificationRequest.builder()
                .email("existingEmail@example.com")
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .emailVerifiedAt(new Date())
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        assertThrows(CustomException.class, () -> authenticationService.userAskVerificationToken(request));
    }


    @Test
    void testUserAskVerificationToken_ExistingUnexpiredToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setEmailVerifiedAt(null);

        VerificationTokenOtp token = new VerificationTokenOtp();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        token.setExpiryDate(calendar.getTime());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(verificationTokenOtpRepository.findByUserEmail("test@example.com")).thenReturn(token);

        UserVerificationRequest userVerificationRequest =  new UserVerificationRequest("test@example.com");
        assertThrows(CustomException.class, () -> authenticationService.userAskVerificationToken(userVerificationRequest));


    }


    @Test
    void testUserAskVerificationToken_DeleteVerificationToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setEmailVerifiedAt(null);

        VerificationTokenOtp token = new VerificationTokenOtp();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1); // Use a negative value to set it to the past
        token.setExpiryDate(calendar.getTime());

        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(verificationTokenOtpRepository.findByUserEmail("test@example.com")).thenReturn(token);

        UserVerificationRequest userVerificationRequest = new UserVerificationRequest("test@example.com");
        authenticationService.userAskVerificationToken(userVerificationRequest);

        verify(verificationTokenOtpRepository, times(1)).delete(eq(token));
    }


}