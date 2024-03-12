//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.User;
//import fontys.emergencywebapps.persistence.entities.VerificationTokenOtp;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.Date;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class VerificationTokenOtpRepositoryTest {
//
//    @Autowired
//    private VerificationTokenOtpRepository verificationTokenOtpRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    void testFindByOtp() {
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
//        VerificationTokenOtp verificationTokenOtp = VerificationTokenOtp.builder()
//                .otp("123456")
//                .expiryDate(new Date())
//                .user(user)
//                .build();
//
//        entityManager.persist(verificationTokenOtp);
//
//        entityManager.flush();
//        VerificationTokenOtp foundToken = verificationTokenOtpRepository.findByOtp("123456");
//
//        assertThat(foundToken).isNotNull();
//        assertThat(foundToken.getOtp()).isEqualTo("123456");
//    }
//
//    @Test
//    void testFindByUserEmail() {
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
//        VerificationTokenOtp verificationTokenOtp = VerificationTokenOtp.builder()
//                .otp("654321")
//                .expiryDate(new Date())
//                .user(user)
//                .build();
//
//        entityManager.persist(verificationTokenOtp);
//        entityManager.flush();
//
//        VerificationTokenOtp foundToken = verificationTokenOtpRepository.findByUserEmail("testEmail@example.com");
//
//        assertThat(foundToken).isNotNull();
//        assertThat(foundToken.getUser().getEmail()).isEqualTo("testEmail@example.com");
//    }
//
//}