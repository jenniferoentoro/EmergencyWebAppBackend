package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.NewsRequest;
import fontys.emergencywebapps.controllers.dto.NewsByCategoriesRequest;
import fontys.emergencywebapps.persistence.entities.News;

import java.io.IOException;

public interface NewsUseCases {
    News getNewsById(Long id);
    Iterable<News> getNewsByTitle(String title);

    News createNews(NewsRequest request) throws IOException;

    News updateNews(Long id, NewsRequest request) throws IOException;

    void deleteNews(Long id);

    Iterable<News> getNews();

    Iterable<News> getNewsByCategories(NewsByCategoriesRequest request);

}
