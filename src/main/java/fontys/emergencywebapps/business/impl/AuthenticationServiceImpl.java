package fontys.emergencywebapps.business.impl;


import java.util.*;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessTokenEncoder;
import fontys.emergencywebapps.controllers.dto.*;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.entities.UserRoles;
import fontys.emergencywebapps.persistence.entities.VerificationTokenOtp;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import fontys.emergencywebapps.persistence.repos.VerificationTokenOtpRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import fontys.emergencywebapps.business.AuthenticationUseCases;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.persistence.entities.Role;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationUseCases {
    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenOtpRepository verificationTokenOtpRepository;

    private final AuthenticationManager authenticationManager;

    private final AccessTokenEncoder jwtService;


    public UserAuthenticationResponse register(UserRegisterRequest request) {

        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }
        if (userRepo.findByIdentityCardNumber(request.getIdentityCardNumber()).isPresent()) {
            throw new CustomException("Identity Card Number already exists");
        }
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }
        if (userRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new CustomException("Phone Number already exists");
        }

        if (request.getPassword().length() < 8) {
            throw new CustomException("Password must be at least 8 characters long");

        }


        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .password(passwordEncoder.encode(request.getPassword())).address(request.getAddress()).email(request.getEmail()).lastName(request.getLastName()).phoneNumber(request.getPhoneNumber()).identityCardNumber(request.getIdentityCardNumber()).birthDate(request.getBirthDate()).emailVerifiedAt(null).build();

        Set<UserRoles> userRoles = new HashSet<>();
        for (String role : request.getRoles()) {
            userRoles.add(UserRoles.builder().role(Role.valueOf(role)).user(user).build());
        }
        user.setRoles(userRoles);


        userRepo.save(user);

        List<String> roles = user.getRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();


        var jwtToken = jwtService.encode(new AccessTokenImpl(user.getUsername(), user.getId(), roles));

        return UserAuthenticationResponse.builder().token(jwtToken).build();

    }

    public UserAuthenticationResponse authenticate(UserLoginRequest request) {
        User user;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword()));
            user = userRepo.findByUsername(request.getUsername()).orElseThrow();
        } catch (Exception e) {
            throw new CustomException("Invalid username or password");
        }
        if (user.getEmailVerifiedAt() == null) {
            throw new CustomException("Email not verified, please check your email for the verification link");
        }
        List<String> roles = user.getRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();
        var jwtToken = jwtService.encode(new AccessTokenImpl(user.getUsername(), user.getId(), roles));
        return UserAuthenticationResponse.builder().token(jwtToken).build();
    }

    public UserAuthenticationResponse authenticateCompare(UserLoginRequest request) {
        User user = userRepo.findByUsername(request.getUsername()).orElseThrow();

        if (!Objects.equals(request.getPassword(), user.getPassword())) {

            System.out.println("password salah");
            throw new CustomException("Invalid username or password");
        }

        if (user.getEmailVerifiedAt() == null) {
            throw new CustomException("Email not verified, please check your email for the verification link");
        }
        System.out.println("password benar");
        List<String> roles = user.getRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();
        System.out.println("roles" + roles);
        var jwtToken = jwtService.encode(new AccessTokenImpl(user.getUsername(), user.getId(), roles));
        System.out.println("jwtToken" + jwtToken);
        return UserAuthenticationResponse.builder().token(jwtToken).build();
    }


    @Override
    public UserAuthenticationResponse registerUser(UserRegisterForUserRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }
        if (userRepo.findByIdentityCardNumber(request.getIdentityCardNumber()).isPresent()) {
            throw new CustomException("Identity Card Number already exists");
        }
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }
        if (userRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new CustomException("Phone Number already exists");
        }

        if (request.getPassword().length() < 8) {
            throw new CustomException("Password must be at least 8 characters long");

        }

        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .password(passwordEncoder.encode(request.getPassword())).address(request.getAddress()).email(request.getEmail()).lastName(request.getLastName()).phoneNumber(request.getPhoneNumber()).identityCardNumber(request.getIdentityCardNumber()).birthDate(request.getBirthDate()).emailVerifiedAt(null).build();

        Set<UserRoles> userRoles = new HashSet<>();
        userRoles.add(UserRoles.builder().role(Role.USER).user(user).build());
        user.setRoles(userRoles);

        userRepo.save(user);

        List<String> roles = user.getRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        var jwtToken = jwtService.encode(new AccessTokenImpl(user.getUsername(), user.getId(), roles));

        return UserAuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public User userAskVerificationToken(UserVerificationRequest request) {
        User user;
        try {
            user = userRepo.findByEmail(request.getEmail()).orElseThrow();
        } catch (Exception e) {
            throw new CustomException("Email not found, please register first");
        }
        if (user.getEmailVerifiedAt() != null) {
            throw new CustomException("Email already verified");
        }


        VerificationTokenOtp verificationTokenOtp = verificationTokenOtpRepository.findByUserEmail(user.getEmail());
        if (verificationTokenOtp != null) {
            Calendar calendar = Calendar.getInstance();
            if ((verificationTokenOtp.getExpiryDate().getTime() - calendar.getTime().getTime()) > 0) {
                throw new CustomException("Token already sent, please check your email for the verification link. You can request a new token after the current one expires");
            } else {
                verificationTokenOtpRepository.delete(verificationTokenOtp);
            }
        }


        return user;
    }


}