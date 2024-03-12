package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.persistence.entities.News;
import fontys.emergencywebapps.persistence.entities.NewsHighlight;
import fontys.emergencywebapps.persistence.repos.NewsHighlightRepository;
import fontys.emergencywebapps.persistence.repos.NewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {NewsHighlightServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class NewsHighlightServiceImplTest {
    @Mock
    private NewsHighlightRepository newsHighlightRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsHighlightServiceImpl newsHighlightService;

    @Test
    void testAddNewsHighlight_NewsHighlightAlreadyExists_ShouldThrowCustomException() {
        when(newsHighlightRepository.findByNewsId(any())).thenReturn(new NewsHighlight());

        assertThrows(CustomException.class, () -> newsHighlightService.addNewsHighlight(1L), "News Highlight already exist");

        verify(newsHighlightRepository, times(0)).save(any());
        verify(newsRepository, times(0)).findById(any());
    }

    @Test
    void testAddNewsHighlight_NewsNotFound_ShouldThrowCustomException() {
        when(newsHighlightRepository.findByNewsId(any())).thenReturn(null);
        when(newsRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsHighlightService.addNewsHighlight(1L), "News not found");

        verify(newsHighlightRepository, times(0)).save(any());
        verify(newsRepository, times(1)).findById(any());
    }

    @Test
    void testAddNewsHighlight_SuccessfullyAddedNewsHighlight_ShouldReturnNews() {
        when(newsHighlightRepository.findByNewsId(any())).thenReturn(null);
        News news = new News();
        when(newsRepository.findById(any())).thenReturn(Optional.of(news));

        News result = newsHighlightService.addNewsHighlight(1L);

        assertNotNull(result);
        assertEquals(news, result);

        verify(newsHighlightRepository, times(1)).save(any());
        verify(newsRepository, times(1)).findById(any());
    }

    @Test
    void testRemoveNewsHighlight_NewsHighlightNotFound_ShouldThrowCustomException() {
        when(newsHighlightRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsHighlightService.removeNewsHighlight(1L), "News Highlight not found");

        verify(newsHighlightRepository, times(0)).delete(any());
    }

    @Test
    void testRemoveNewsHighlight_SuccessfullyRemovedNewsHighlight_ShouldNotThrowException() {
        when(newsHighlightRepository.findById(any())).thenReturn(Optional.of(new NewsHighlight()));

        assertDoesNotThrow(() -> newsHighlightService.removeNewsHighlight(1L));

        verify(newsHighlightRepository, times(1)).delete(any());
    }

    @Test
    void testGetNewsHighlight_ShouldReturnList() {
        List<NewsHighlight> newsHighlightList = List.of(new NewsHighlight(), new NewsHighlight());
        when(newsHighlightRepository.findAll()).thenReturn(newsHighlightList);

        List<NewsHighlight> result = newsHighlightService.getNewsHighlight();

        assertNotNull(result);
        assertEquals(newsHighlightList.size(), result.size());

        verify(newsHighlightRepository, times(1)).findAll();
    }
}