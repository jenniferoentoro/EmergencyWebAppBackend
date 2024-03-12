package fontys.emergencywebapps.persistence.repos;

import fontys.emergencywebapps.persistence.entities.Role;
import fontys.emergencywebapps.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Optional<User> findByIdentityCardNumber(String identityCardNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);


    @Query("SELECT DISTINCT u FROM User u JOIN UserRoles ur ON u.id = ur.user.id WHERE ur.role = 'ADMIN' OR ur.role = 'HELPER'")
    List<User> findAdmins();


    @Query("SELECT u FROM User u JOIN UserRoles ur ON u.id = ur.user.id WHERE ur.role = 'USER'")
    List<User> findUsers();

    @Query("SELECT u FROM User u " +
            "JOIN ChatConnection c ON c.user.id = u.id " +
            "WHERE c.statusChatConnection = 'ONGOING'")
    List<User> findUserIdsAndUsernamesWithOngoingChatConnection();


}
