package fontys.emergencywebapps.business;

import fontys.emergencywebapps.persistence.entities.News;
import fontys.emergencywebapps.persistence.entities.NewsHighlight;

import java.util.List;

public interface NewsHighlightUseCases {
    News addNewsHighlight(Long idNews);

    void removeNewsHighlight(Long idNews);

    List<NewsHighlight> getNewsHighlight();
}
