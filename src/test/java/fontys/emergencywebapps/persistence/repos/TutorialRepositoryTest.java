//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import fontys.emergencywebapps.persistence.entities.Tutorial;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class TutorialRepositoryTest {
//
//    @Autowired
//    private TutorialRepository tutorialRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    void testFindAllByTitleContaining() {
//
//
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("testIncidentCategory")
//                .build();
//
//        entityManager.persist(incidentCategory);
//        Tutorial tutorial = Tutorial.builder()
//                .title("Java Programming Tutorial")
//                .incidentCategory(incidentCategory)
//                .description("Learn Java programming")
//                .linkToVideo("https://www.youtube.com/watch?v=eIrMbAQSU34")
//                .build();
//
//        entityManager.persist(tutorial);
//
//        entityManager.flush();
//
//        Iterable<Tutorial> foundTutorials = tutorialRepository.findAllByTitleContaining("Java");
//
//        assertThat(foundTutorials).isNotEmpty();
//        assertThat(foundTutorials.iterator().next().getTitle()).isEqualTo("Java Programming Tutorial");
//    }
//
//    @Test
//    void testFindAllByTitleContaining_NoMatch() {
//
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("testIncidentCategory")
//                .build();
//
//        entityManager.persist(incidentCategory);
//        Tutorial tutorial = Tutorial.builder()
//                .title("Java Programming Tutorial")
//                .incidentCategory(incidentCategory)
//                .description("Learn Java programming")
//                .linkToVideo("https://www.youtube.com/watch?v=eIrMbAQSU34")
//                .build();
//
//        entityManager.persist(tutorial);
//
//        entityManager.flush();
//        Iterable<Tutorial> foundTutorials = tutorialRepository.findAllByTitleContaining("Python");
//
//        assertThat(foundTutorials).isEmpty();
//    }
//}