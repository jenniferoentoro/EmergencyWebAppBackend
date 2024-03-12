//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class IncidentCategoryRepositoryTest {
//    @Autowired
//    private IncidentCategoryRepository incidentCategoryRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//
//    @Test
//    void testFindByName() {
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("testIncidentCategory")
//                .build();
//        entityManager.persist(incidentCategory);
//        entityManager.flush();
//
//        IncidentCategory foundIncidentCategory = incidentCategoryRepository.findByName("testIncidentCategory");
//
//        assertThat(foundIncidentCategory.getName()).isEqualTo("testIncidentCategory");
//
//    }
//
//    @Test
//     void testDeleteAllByIdIn() {
//        IncidentCategory category1 = new IncidentCategory();
//        category1.setName("Category1");
//        entityManager.persist(category1);
//
//        IncidentCategory category2 = new IncidentCategory();
//        category2.setName("Category2");
//        entityManager.persist(category2);
//
//        entityManager.flush();
//
//        List<Long> idsToDelete = List.of(category1.getId(), category2.getId());
//
//        incidentCategoryRepository.deleteAllByIdIn(idsToDelete);
//
//        assertNull(incidentCategoryRepository.findByName("Category1"));
//        assertNull(incidentCategoryRepository.findByName("Category2"));
//    }
//}