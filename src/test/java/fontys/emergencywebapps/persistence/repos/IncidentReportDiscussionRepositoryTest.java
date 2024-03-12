//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import fontys.emergencywebapps.persistence.entities.IncidentReport;
//import fontys.emergencywebapps.persistence.entities.IncidentReportDiscussion;
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
//class IncidentReportDiscussionRepositoryTest {
//
//    @Autowired
//    private IncidentReportDiscussionRepository incidentReportDiscussionRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindAllByIncidentReportId() {
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("testIncidentCategory")
//                .build();
//        entityManager.persist(incidentCategory);
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
//
//        entityManager.persist(user);
//        IncidentReport incidentReport = IncidentReport.builder()
//                .incidentCategory(incidentCategory)
//                .title("Incident Title")
//                .description("Incident Description")
//                .photoUrl("http://example.com/photo.jpg")
//                .longitude(123.0)
//                .latitude(456.0)
//                .date(new Date())
//                .user(user)
//                .statusFixed(false)
//                .build();
//
//        entityManager.persist(incidentReport);
//
//        // Create and persist the IncidentReportDiscussion entities associated with the incidentReport
//        IncidentReportDiscussion discussion1 = new IncidentReportDiscussion();
//        discussion1.setIncidentReport(incidentReport);
//        discussion1.setDescription("Discussion 1");
//        entityManager.persist(discussion1);
//
//        IncidentReportDiscussion discussion2 = new IncidentReportDiscussion();
//        discussion2.setIncidentReport(incidentReport);
//        discussion2.setDescription("Discussion 2");
//        entityManager.persist(discussion2);
//
//        // Create and persist another IncidentReport
//        IncidentReport unRelatedIncidentReport = IncidentReport.builder()
//                .incidentCategory(incidentCategory)
//                .title("Incident Title 2")
//                .description("Incident Description")
//                .photoUrl("http://example.com/photo.jpg")
//                .longitude(123.0)
//                .latitude(456.0)
//                .date(new Date())
//                .user(user)
//                .statusFixed(false)
//                .build();
//
//        entityManager.persist(unRelatedIncidentReport);
//
//        // Create and persist an unrelated IncidentReportDiscussion associated with the unRelatedIncidentReport
//        IncidentReportDiscussion unrelatedDiscussion = new IncidentReportDiscussion();
//        unrelatedDiscussion.setIncidentReport(unRelatedIncidentReport);
//        unrelatedDiscussion.setDescription("Unrelated Discussion");
//        entityManager.persist(unrelatedDiscussion);
//
//        entityManager.flush();
//
//        Iterable<IncidentReportDiscussion> discussions = incidentReportDiscussionRepository.findAllByIncidentReportId(incidentReport.getId());
//
//        assertThat(discussions).isNotEmpty();
//        assertThat(discussions).hasSize(2);
//    }
//
//}