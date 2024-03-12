package fontys.emergencywebapps.business;

import fontys.emergencywebapps.persistence.entities.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserUseCases {
    User getUser(String username);
    Iterable<User> getUsers();

    void createVerificationToken(User theUser, String verificationToken);

    String validateVerificationToken(String token);

    User getUserFromEmail(String email);

    User getMe(HttpServletRequest servletRequest);

    List<User> getChatAdmins();

    List<User> getAdmins();

    List<User> getUsersRole();

}
