//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import fontys.emergencywebapps.persistence.entities.News;
//import fontys.emergencywebapps.persistence.entities.NewsHighlight;
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
//class NewsHighlightRepositoryTest {
//    @Autowired
//    private NewsHighlightRepository newsHighlightRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindByNewsId() {
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("testIncidentCategory")
//                .build();
//        entityManager.persist(incidentCategory);
//        News news = News.builder()
//                .title("News Headline").date(new Date()).description("News Description").image("https://example.com/image.jpg")
//                .incidentCategory(incidentCategory)
//                .build();
//        entityManager.persist(news);
//        NewsHighlight newsHighlight = NewsHighlight.builder()
//                .news(news)
//                .build();
//        entityManager.persist(newsHighlight);
//
//        entityManager.flush();
//
//        NewsHighlight foundNewsHighlight = newsHighlightRepository.findByNewsId(newsHighlight.getNews().getId());
//
//        assertThat(foundNewsHighlight).isNotNull();
//        assertThat(foundNewsHighlight.getNews().getTitle()).isEqualTo("News Headline");
//    }
//}