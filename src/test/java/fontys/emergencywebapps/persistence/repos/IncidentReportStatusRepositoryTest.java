//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentReport;
//import fontys.emergencywebapps.persistence.entities.IncidentReportStatus;
//import fontys.emergencywebapps.persistence.entities.Status;
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
//class IncidentReportStatusRepositoryTest {
//
//    @Autowired
//    private IncidentReportStatusRepository incidentReportStatusRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindByIncidentReportId() {
//        IncidentReport incidentReport = IncidentReport.builder()
//                .title("Incident Title")
//                .description("Incident Description")
//                .photoUrl("https://example.com/photo.jpg")
//                .longitude(123.0)
//                .latitude(456.0)
//                .statusFixed(false)
//                .build();
//        entityManager.persist(incidentReport);
//
//        IncidentReportStatus status1 = IncidentReportStatus.builder()
//                .status(Status.WAITING).description("testDescription")
//                .incidentReport(incidentReport).date(new Date())
//                .build();
//        entityManager.persist(status1);
//
//        IncidentReportStatus status2 = IncidentReportStatus.builder()
//                .status(Status.WAITING).description("testDescription")
//                .date(new Date())
//                .incidentReport(incidentReport)
//                .build();
//        entityManager.persist(status2);
//
//        entityManager.flush();
//
//        Iterable<IncidentReportStatus> statuses = incidentReportStatusRepository.findByIncidentReportId(incidentReport.getId());
//
//        assertThat(statuses).isNotEmpty();
//        assertThat(statuses).hasSize(2);
//    }
//}