//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.Date;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class MessageRepositoryTest {
//
//    @Autowired
//    private MessageRepository messageRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindMessagesByUserIdAndOngoingChatConnection() {
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
//
//        ChatConnection chatConnection = ChatConnection.builder()
//                .user(user)
//                .statusChatConnection(StatusChatConnection.ONGOING).latitude("0.0").longitude("0.0").typeConnection(TypeConnection.OPENAI)
//                .build();
//
//        entityManager.persist(chatConnection);
//
//        Message message = Message.builder()
//                .chatConnection(chatConnection).sender(user).statusMsg(StatusMsg.SENT).date(new Date()).message("Test message content").build();
//        entityManager.persist(message);
//
//        User otherUser = User.builder()
//                .username("testUser2")
//                .password("testPassword")
//                .email("testEmail2@example.com")
//                .phoneNumber("testPhoneNumber2")
//                .identityCardNumber("testIdentityCardNumber2")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .address("testAddress")
//                .birthDate(birthDate)
//                .emailVerifiedAt(null)
//                .build();
//        entityManager.persist(otherUser);
//
//        ChatConnection otherChatConnection = ChatConnection.builder()
//                .user(otherUser)
//                .statusChatConnection(StatusChatConnection.ONGOING).latitude("0.0").longitude("0.0").typeConnection(TypeConnection.OPENAI)
//                .build();
//
//        entityManager.persist(otherChatConnection);
//
//        Message otherMessage = Message.builder()
//                .chatConnection(otherChatConnection).sender(otherUser).statusMsg(StatusMsg.SENT).date(new Date()).message("Hai").build();
//        entityManager.persist(message);
//
//        entityManager.persist(otherMessage);
//
//        entityManager.flush();
//        List<Message> messages = messageRepository.findMessagesByUserIdAndOngoingChatConnection("testUser");
//
//        assertThat(messages).isNotEmpty();
//        assertThat(messages).hasSize(1);
//        assertThat(messages.get(0).getMessage()).isEqualTo("Test message content");
//    }
//
//}