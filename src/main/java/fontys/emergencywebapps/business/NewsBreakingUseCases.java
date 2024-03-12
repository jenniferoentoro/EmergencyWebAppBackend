package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.NewsBreakingRequest;
import fontys.emergencywebapps.persistence.entities.NewsBreaking;

import java.util.List;

public interface NewsBreakingUseCases {
    NewsBreaking addNewsBreaking(NewsBreakingRequest newsBreakingRequest);

    void removeNewsHighlight(Long id);

    List<NewsBreaking> getNewsBreaking();
}
