package fontys.emergencywebapps.controllers;


import fontys.emergencywebapps.business.impl.RefreshTokenServiceImpl;
import fontys.emergencywebapps.controllers.dto.*;
import fontys.emergencywebapps.persistence.entities.RefreshToken;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.entities.VerificationTokenOtp;
import fontys.emergencywebapps.persistence.repos.VerificationTokenOtpRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.business.impl.AuthenticationServiceImpl;
import fontys.emergencywebapps.business.impl.UserServiceImpl;
import fontys.emergencywebapps.configurations.event.RegistrationCompleteEvent;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private final AuthenticationServiceImpl authService;
    private final UserServiceImpl userService;

    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenOtpRepository verificationTokenOtpRepository;
    private final ModelMapper modelMapper;

    private final RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest request, HttpServletRequest httpServletRequest) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new CustomException("Password and confirm password are not the same");
        }
        authService.register(request);
        User user = userService.getUser(request.getUsername());
        UserGetDTO userGetDTO = UserGetDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .identityCardNumber(user.getIdentityCardNumber())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();
        eventPublisher.publishEvent(new RegistrationCompleteEvent(userGetDTO, applicationUrl(httpServletRequest)));
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully, please check your email for activation link.");
    }

//    register for user page (cant modify the role)
    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterForUserRequest request, HttpServletRequest httpServletRequest) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new CustomException("Password and confirm password are not the same");
        }
        authService.registerUser(request);
        User user = userService.getUser(request.getUsername());
        UserGetDTO userGetDTO = UserGetDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .identityCardNumber(user.getIdentityCardNumber())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();
        eventPublisher.publishEvent(new RegistrationCompleteEvent(userGetDTO, applicationUrl(httpServletRequest)));
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully, please check your email for activation link.");
    }

    @PostMapping("/resend-token")
    public ResponseEntity<String> resendToken(@Valid @RequestBody UserVerificationRequest request, HttpServletRequest httpServletRequest) {
        User user = authService.userAskVerificationToken(request);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(modelMapper.map(user, UserGetDTO.class), applicationUrl(httpServletRequest)));
        return ResponseEntity.status(HttpStatus.CREATED).body("Please check your email for activation link.");
    }

    private String applicationUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserLoginRequest request) {


        return ResponseEntity.ok().body(UserAuthenticationRefreshResponse.builder()
                .token(authService.authenticate(request).getToken())
                .refreshToken(refreshTokenService.createRefreshToken(request.getUsername()).getToken())
                .build());
    }



    @GetMapping("/registrationConfirm")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationTokenOtp verificationTokenOtp = verificationTokenOtpRepository.findByOtp(token);

        if (verificationTokenOtp == null) {
            throw new CustomException("Invalid token");
        }

        if (verificationTokenOtp.getUser().getEmailVerifiedAt() != null) {
            return "This account has already been verified, please login.";
        }
        return userService.validateVerificationToken(token);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .build();
                    String newToken = authService.authenticateCompare(userLoginRequest).getToken();

                    return ResponseEntity.ok().body(UserAuthenticationRefreshResponse.builder()
                            .token(newToken)
                            .refreshToken(request.getToken())
                            .build());
                })
                .orElseThrow(() -> new CustomException("Invalid refresh token"));
    }

}
