//package fontys.emergencywebapps.persistence.repos;
//
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import fontys.emergencywebapps.persistence.entities.News;
//import fontys.emergencywebapps.persistence.entities.NewsBreaking;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.Date;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class NewsBreakingRepositoryTest {
//    @Autowired
//    private NewsBreakingRepository newsBreakingRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//     void testFindByNewsId() {
//
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .name("testIncidentCategory")
//                .build();
//        entityManager.persist(incidentCategory);
//        News news = News.builder()
//                .title("News Headline").date(new Date()).description("News Description").image("https://example.com/image.jpg")
//                .incidentCategory(incidentCategory)
//                .build();
//        entityManager.persist(news);
//        NewsBreaking newsBreaking = NewsBreaking.builder()
//                .title("Breaking News Headline").news(news)
//                .build();
//        entityManager.persist(newsBreaking);
//
//        entityManager.flush();
//
//        Optional<NewsBreaking> foundNewsBreaking = newsBreakingRepository.findByNewsId(newsBreaking.getNews().getId());
//
//        assertThat(foundNewsBreaking).isPresent();
//        assertThat(foundNewsBreaking.get().getTitle()).isEqualTo("Breaking News Headline");
//    }
//}