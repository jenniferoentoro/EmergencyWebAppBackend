//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.ChatConnection;
//import fontys.emergencywebapps.persistence.entities.StatusChatConnection;
//import fontys.emergencywebapps.persistence.entities.TypeConnection;
//import fontys.emergencywebapps.persistence.entities.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.Date;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class ChatConnectionRepositoryTest {
//
//    @Autowired
//    private ChatConnectionRepository chatConnectionRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindByUserIdAndStatusChatConnection() {
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
//
//        ChatConnection chatConnection = ChatConnection.builder()
//                .user(user)
//                .statusChatConnection(StatusChatConnection.ONGOING).latitude("0.0").longitude("0.0").typeConnection(TypeConnection.OPENAI)
//                .build();
//
//        entityManager.persist(chatConnection);
//        entityManager.flush();
//
//        ChatConnection foundConnection = chatConnectionRepository.findByUserIdAndStatusChatConnection(user.getId(), StatusChatConnection.ONGOING);
//
//        assertThat(foundConnection).isNotNull();
//        assertThat(foundConnection.getUser().getUsername()).isEqualTo("testUser");
//    }
//
//    @Test
//     void testFindByUsernameAndStatusChatConnectionCustom() {
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
//        entityManager.persist(user);
//        ChatConnection chatConnection = ChatConnection.builder()
//                .user(user)
//                .statusChatConnection(StatusChatConnection.ONGOING).latitude("0.0").longitude("0.0").typeConnection(TypeConnection.OPENAI)
//                .build();
//
//        entityManager.persist(chatConnection);
//
//        entityManager.flush();
//
//        ChatConnection foundConnection = chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom("testUser", StatusChatConnection.ONGOING);
//
//        assertThat(foundConnection).isNotNull();
//        assertThat(foundConnection.getUser().getUsername()).isEqualTo("testUser");
//    }
//}