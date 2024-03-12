//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.ChatConnection;
//import fontys.emergencywebapps.persistence.entities.StatusChatConnection;
//import fontys.emergencywebapps.persistence.entities.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class UserRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindByUsername() {
//        Date birthDate = new Date();
//        User user = User.builder()
//                .username("testUser")
//                .password("testPassword")
//                .email("testEmail@example.com")
//                .phoneNumber("testPhoneNumber")
//                .identityCardNumber("testIdentityCardNumber")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .address("testAddress")
//                .birthDate(birthDate)
//                .emailVerifiedAt(null)
//                .build();
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        Optional<User> foundUser = userRepository.findByUsername("testUser");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getUsername()).isEqualTo("testUser");
//    }
//
//    @Test
//     void testFindByEmail() {
//        Date birthDate = new Date();
//        User user = User.builder()
//                .username("testUser")
//                .password("testPassword")
//                .email("testEmail@example.com")
//                .phoneNumber("testPhoneNumber")
//                .identityCardNumber("testIdentityCardNumber")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .address("testAddress")
//                .birthDate(birthDate)
//                .emailVerifiedAt(null)
//                .build();
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        Optional<User> foundUser = userRepository.findByEmail("testEmail@example.com");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getEmail()).isEqualTo("testEmail@example.com");
//    }
//
//    @Test
//     void testFindByIdentityCardNumber() {
//        Date birthDate = new Date();
//        User user = User.builder()
//                .username("testUser")
//                .password("testPassword")
//                .email("testEmail@example.com")
//                .phoneNumber("testPhoneNumber")
//                .identityCardNumber("testIdentityCardNumber")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .address("testAddress")
//                .birthDate(birthDate)
//                .emailVerifiedAt(null)
//                .build();
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        Optional<User> foundUser = userRepository.findByIdentityCardNumber("testIdentityCardNumber");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getIdentityCardNumber()).isEqualTo("testIdentityCardNumber");
//    }
//
//    @Test
//     void testFindByPhoneNumber() {
//        Date birthDate = new Date();
//        User user = User.builder()
//                .username("testUser")
//                .password("testPassword")
//                .email("testEmail@example.com")
//                .phoneNumber("testPhoneNumber")
//                .identityCardNumber("testIdentityCardNumber")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .address("testAddress")
//                .birthDate(birthDate)
//                .emailVerifiedAt(null)
//                .build();
//
//        entityManager.persist(user);
//        entityManager.flush();
//
//        Optional<User> foundUser = userRepository.findByPhoneNumber("testPhoneNumber");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getPhoneNumber()).isEqualTo("testPhoneNumber");
//    }
//    @Test
//     void testFindUserIdsAndUsernamesWithOngoingChatConnection() {
//        User userWithOngoingChat = User.builder()
//                .username("user1")
//                .password("password1")
//                .email("user1@example.com")
//                .phoneNumber("1234567891")
//                .identityCardNumber("ID123")
//                .firstName("User")
//                .lastName("One")
//                .address("123 Main St")
//                .birthDate(new Date())
//                .emailVerifiedAt(new Date())
//                .build();
//        entityManager.persist(userWithOngoingChat);
//
//        ChatConnection ongoingChatConnection = new ChatConnection();
//        ongoingChatConnection.setUser(userWithOngoingChat);
//        ongoingChatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);
//        ongoingChatConnection.setLatitude("123.456"); // Set a non-null latitude value
//        ongoingChatConnection.setLongitude("789.012"); // Set a non-null longitude value
//        entityManager.persist(ongoingChatConnection);
//
//        User userWithoutOngoingChat = User.builder()
//                .username("user2")
//                .password("password2")
//                .email("user2@example.com")
//                .phoneNumber("1234567892")
//                .identityCardNumber("ID456")
//                .firstName("User")
//                .lastName("Two")
//                .address("456 Elm St")
//                .birthDate(new Date())
//                .emailVerifiedAt(new Date())
//                .build();
//        entityManager.persist(userWithoutOngoingChat);
//
//        List<User> usersWithOngoingChat = userRepository.findUserIdsAndUsernamesWithOngoingChatConnection();
//
//        System.out.println(usersWithOngoingChat + "yup");
//        assertNotNull(usersWithOngoingChat);
//        int lastIndex = usersWithOngoingChat.size() - 1;
//        assertEquals(userWithOngoingChat.getId(), usersWithOngoingChat.get(lastIndex).getId());
//        assertEquals(userWithOngoingChat.getUsername(), usersWithOngoingChat.get(lastIndex).getUsername());
//
//
//
//
//    }
//
//}