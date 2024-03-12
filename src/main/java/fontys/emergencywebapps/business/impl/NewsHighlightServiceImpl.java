package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.persistence.entities.News;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.NewsHighlightUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.persistence.entities.NewsHighlight;
import fontys.emergencywebapps.persistence.repos.NewsHighlightRepository;
import fontys.emergencywebapps.persistence.repos.NewsRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsHighlightServiceImpl implements NewsHighlightUseCases {
    private final NewsHighlightRepository newsHighlightRepository;
    private final NewsRepository newsRepository;
    @Override
    public News addNewsHighlight(Long idNews) {
        NewsHighlight newsHighlight = newsHighlightRepository.findByNewsId(idNews);
        if(newsHighlight != null){
            throw new CustomException("News Highlight already exist");
        }
        Optional<News> newsRequest = newsRepository.findById(idNews);
        if(newsRequest.isEmpty()){
            throw new CustomException("News not found");
        }
        newsHighlightRepository.save(NewsHighlight.builder().news(newsRequest.get()).build());
        return newsRequest.get();
    }

    @Override
    public void removeNewsHighlight(Long id) {

        Optional<NewsHighlight> newsHighlight = newsHighlightRepository.findById(id);
        if(newsHighlight.isEmpty()){
            throw new CustomException("News Highlight not found");
        }
        newsHighlightRepository.delete(newsHighlight.get());
    }

    @Override
    public List<NewsHighlight> getNewsHighlight() {
        return newsHighlightRepository.findAll();

    }
}
