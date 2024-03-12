//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import fontys.emergencywebapps.persistence.entities.News;
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
//class NewsRepositoryTest {
//
//    @Autowired
//    private NewsRepository newsRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindByTitleContaining() {
//        News news = News.builder()
//                .title("News Headline")
//                .date(new Date())
//                .description("News Description")
//                .image("https://example.com/image.jpg")
//                .build();
//
//        entityManager.persist(news);
//
//        Iterable<News> foundNews = newsRepository.findByTitleContaining("Headline");
//
//        assertThat(foundNews).isNotEmpty();
//        assertThat(foundNews.iterator().next().getTitle()).isEqualTo("News Headline");
//    }
//
//    @Test
//     void testFindByIncidentCategory() {
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("testIncidentCategory")
//                .build();
//
//        entityManager.persist(incidentCategory);
//
//        News news = News.builder()
//                .title("News Headline")
//                .date(new Date())
//                .description("News Description")
//                .image("https://example.com/image.jpg")
//                .incidentCategory(incidentCategory)
//                .build();
//
//        entityManager.persist(news);
//
//        List<News> foundNews = newsRepository.findByIncidentCategory(incidentCategory);
//
//        assertThat(foundNews).isNotEmpty();
//        assertThat(foundNews.get(0).getIncidentCategory().getName()).isEqualTo("testIncidentCategory");
//    }
//}