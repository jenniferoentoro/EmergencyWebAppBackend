package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.persistence.entities.News;
import fontys.emergencywebapps.persistence.repos.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.NewsBreakingUseCases;
import fontys.emergencywebapps.controllers.dto.NewsBreakingRequest;
import fontys.emergencywebapps.persistence.entities.NewsBreaking;
import fontys.emergencywebapps.persistence.repos.NewsBreakingRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsBreakingServiceImpl implements NewsBreakingUseCases {
private final NewsRepository newsRepository;
private final NewsBreakingRepository newsBreakingRepository;
    @Override
    public NewsBreaking addNewsBreaking(NewsBreakingRequest newsBreakingRequest) {
        Optional<News> news = newsRepository.findById(newsBreakingRequest.getNewsId());
        if(news.isEmpty()){
            throw new CustomException("News not found");
        }
        Optional<NewsBreaking> newsBreaking = newsBreakingRepository.findByNewsId(newsBreakingRequest.getNewsId());
        if(newsBreaking.isPresent()){
            throw new CustomException("News Breaking already exist");
        }
        return newsBreakingRepository.save(NewsBreaking.builder().title(newsBreakingRequest.getTitle()).news(news.get()).build());
    }

    @Override
    public void removeNewsHighlight(Long id) {
        Optional<NewsBreaking> newsBreaking = newsBreakingRepository.findById(id);
        if(newsBreaking.isEmpty()){
            throw new CustomException("News Breaking not found");
        }
        newsBreakingRepository.delete(newsBreaking.get());

    }

    @Override
    public List<NewsBreaking> getNewsBreaking() {
        return newsBreakingRepository.findAll();
    }
}
