//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import fontys.emergencywebapps.persistence.entities.IncidentReport;
//import fontys.emergencywebapps.persistence.entities.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.Date;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class IncidentReportRepositoryTest {
//    @Autowired
//    private IncidentReportRepository incidentReportRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//    @Test
//     void testFindAllByUserId() {
//        User user = User.builder()
//                .username("testUser")
//                .password("testPassword")
//                .email("testEmail@example.com")
//                .phoneNumber("testPhoneNumber")
//                .identityCardNumber("testIdentityCardNumber")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .address("testAddress")
//                .birthDate(new Date())
//                .emailVerifiedAt(null)
//                .build();
//        entityManager.persist(user);
//
//        IncidentReport incidentReport1 = IncidentReport.builder()
//                .title("Incident Title 1")
//                .description("Incident Description 1")
//                .photoUrl("https://example.com/photo.jpg")
//                .longitude(12.3)
//                .latitude(45.6)
//                .user(user)
//                .statusFixed(false)
//                .build();
//        entityManager.persist(incidentReport1);
//
//        IncidentReport incidentReport2 = IncidentReport.builder()
//                .title("Incident Title 2")
//                .description("Incident Description 2")
//                .photoUrl("https://example.com2/photo.jpg")
//                .longitude(78.9)
//                .latitude(10.1)
//                .user(user)
//                .statusFixed(true)
//                .build();
//        entityManager.persist(incidentReport2);
//
//        entityManager.flush();
//
//        Iterable<IncidentReport> incidentReports = incidentReportRepository.findAllByUserId(user.getId());
//
//        assertThat(incidentReports).isNotEmpty();
//        assertThat(incidentReports).hasSize(2);
//    }
//
//    @Test
//     void testFindAllByIncidentCategoryId() {
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("TestCategory")
//                .build();
//        entityManager.persist(incidentCategory);
//
//        IncidentReport incidentReport1 = IncidentReport.builder()
//                .title("Incident Title 1")
//                .description("Incident Description 1")
//                .longitude(12.3)
//                .photoUrl("https://example3.com/photo.jpg")
//                .latitude(45.6)
//                .incidentCategory(incidentCategory)
//                .statusFixed(false)
//                .build();
//        entityManager.persist(incidentReport1);
//
//        IncidentReport incidentReport2 = IncidentReport.builder()
//                .title("Incident Title 2")
//                .description("Incident Description 2")
//                .photoUrl("https://example4.com/photo.jpg")
//                .longitude(78.9)
//                .latitude(10.1)
//                .incidentCategory(incidentCategory)
//                .statusFixed(true)
//                .build();
//        entityManager.persist(incidentReport2);
//
//        entityManager.flush();
//
//        Iterable<IncidentReport> incidentReports = incidentReportRepository.findAllByIncidentCategoryId(incidentCategory.getId());
//
//        assertThat(incidentReports).isNotEmpty();
//        assertThat(incidentReports).hasSize(2);
//    }
//
//    @Test
//     void testFindAllByIncidentCategoryIdAndUserId() {
//        User user = User.builder()
//                .username("testUser")
//                .password("testPassword")
//                .email("testEmail@example.com")
//                .phoneNumber("testPhoneNumber")
//                .identityCardNumber("testIdentityCardNumber")
//                .firstName("testFirstName")
//                .lastName("testLastName")
//                .address("testAddress")
//                .birthDate(new Date())
//                .emailVerifiedAt(null)
//                .build();
//        entityManager.persist(user);
//
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("TestCategory")
//                .build();
//        entityManager.persist(incidentCategory);
//
//        IncidentReport incidentReport1 = IncidentReport.builder()
//                .title("Incident Title 1")
//                .description("Incident Description 1")
//                .photoUrl("https://example.com5/photo.jpg")
//                .longitude(12.3)
//                .latitude(45.6)
//                .user(user)
//                .incidentCategory(incidentCategory)
//                .statusFixed(false)
//                .build();
//        entityManager.persist(incidentReport1);
//
//        IncidentReport incidentReport2 = IncidentReport.builder()
//                .title("Incident Title 2")
//                .description("Incident Description 2")
//                .photoUrl("https://example.com6/photo.jpg")
//                .longitude(78.9)
//                .latitude(10.1)
//                .user(user)
//                .incidentCategory(incidentCategory)
//                .statusFixed(true)
//                .build();
//        entityManager.persist(incidentReport2);
//
//        entityManager.flush();
//
//        Iterable<IncidentReport> incidentReports = incidentReportRepository.findAllByIncidentCategoryIdAndUserId(incidentCategory.getId(), user.getId());
//
//        assertThat(incidentReports).isNotEmpty();
//        assertThat(incidentReports).hasSize(2);
//    }
//
//}