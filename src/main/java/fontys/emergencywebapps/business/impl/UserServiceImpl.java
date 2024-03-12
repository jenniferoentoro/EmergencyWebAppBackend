package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.UserUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.persistence.entities.Role;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.entities.VerificationTokenOtp;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import fontys.emergencywebapps.persistence.repos.VerificationTokenOtpRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserUseCases {
    private final UserRepository userRepository;
    private final VerificationTokenOtpRepository verificationTokenOtpRepository;

    private final AccessTokenDecoder jwtService;
    private static final String NOT_FOUND = "User not found";

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        return user.get();

    }


    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void createVerificationToken(User theUser, String verificationToken) {
        VerificationTokenOtp verificationTokenOtp = new VerificationTokenOtp(verificationToken, theUser);
        verificationTokenOtpRepository.save(verificationTokenOtp);

    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationTokenOtp otp = verificationTokenOtpRepository.findByOtp(token);
        if (otp == null) {
            throw new CustomException("Invalid token");
        }

        User user = otp.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((otp.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationTokenOtpRepository.delete(otp);
            throw new CustomException("Token already expired");
        }
        user.setEmailVerifiedAt(calendar.getTime());
        userRepository.save(user);
        return "Email verified successfully. Now you can login to your account.";
    }

    @Override
    public User getUserFromEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        return user.get();
    }

    @Override
    public User getMe(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Optional<User> user = userRepository.findById(claims.getUserId());
        if (user.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        return user.get();
    }

    @Override
    public List<User> getChatAdmins() {

        List<User> users = userRepository.findUserIdsAndUsernamesWithOngoingChatConnection();
        if (users == null) {
            throw new CustomException("Users not found");
        }

        return users;
    }

    @Override
    public List<User> getAdmins() {
        return userRepository.findAdmins();
    }

    @Override
    public List<User> getUsersRole() {

        return userRepository.findUsers();
    }


}
