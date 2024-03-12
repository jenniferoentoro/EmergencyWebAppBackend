package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.NewsBreakingRequest;
import fontys.emergencywebapps.persistence.entities.News;
import fontys.emergencywebapps.persistence.entities.NewsBreaking;
import fontys.emergencywebapps.persistence.repos.NewsBreakingRepository;
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

@ContextConfiguration(classes = {NewsBreakingServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class NewsBreakingServiceImplTest {
    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsBreakingRepository newsBreakingRepository;

    @InjectMocks
    private NewsBreakingServiceImpl newsBreakingService;

    @Test
    void testAddNewsBreaking_NewsNotFound_ShouldThrowCustomException() {
        NewsBreakingRequest request = new NewsBreakingRequest();
        when(newsRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsBreakingService.addNewsBreaking(request), "News not found");

        verify(newsBreakingRepository, times(0)).findByNewsId(any());
        verify(newsBreakingRepository, times(0)).save(any());
    }

    @Test
    void testAddNewsBreaking_NewsBreakingAlreadyExists_ShouldThrowCustomException() {
        NewsBreakingRequest request = new NewsBreakingRequest();
        when(newsRepository.findById(any())).thenReturn(Optional.of(new News()));
        when(newsBreakingRepository.findByNewsId(any())).thenReturn(Optional.of(new NewsBreaking()));

        assertThrows(CustomException.class, () -> newsBreakingService.addNewsBreaking(request), "News Breaking already exist");

        verify(newsBreakingRepository, times(1)).findByNewsId(any());
        verify(newsBreakingRepository, times(0)).save(any());
    }

    @Test
    void testAddNewsBreaking_SuccessfullyAddedNewsBreaking_ShouldReturnNewsBreaking() {
        NewsBreakingRequest request = new NewsBreakingRequest();
        when(newsRepository.findById(any())).thenReturn(Optional.of(new News()));
        when(newsBreakingRepository.findByNewsId(any())).thenReturn(Optional.empty());
        when(newsBreakingRepository.save(any())).thenReturn(new NewsBreaking());

        NewsBreaking result = newsBreakingService.addNewsBreaking(request);

        assertNotNull(result);

        verify(newsBreakingRepository, times(1)).findByNewsId(any());
        verify(newsBreakingRepository, times(1)).save(any());
    }

    @Test
    void testRemoveNewsBreaking_NewsBreakingNotFound_ShouldThrowCustomException() {
        when(newsBreakingRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsBreakingService.removeNewsHighlight(1L), "News Breaking not found");

        verify(newsBreakingRepository, times(0)).delete(any());
    }

    @Test
    void testRemoveNewsBreaking_SuccessfullyRemovedNewsBreaking_ShouldNotThrowException() {
        when(newsBreakingRepository.findById(any())).thenReturn(Optional.of(new NewsBreaking()));

        assertDoesNotThrow(() -> newsBreakingService.removeNewsHighlight(1L));

        verify(newsBreakingRepository, times(1)).delete(any());
    }

    @Test
    void testGetNewsBreaking_ShouldReturnList() {
        List<NewsBreaking> newsBreakingList = List.of(new NewsBreaking(), new NewsBreaking());
        when(newsBreakingRepository.findAll()).thenReturn(newsBreakingList);

        List<NewsBreaking> result = newsBreakingService.getNewsBreaking();

        assertNotNull(result);
        assertEquals(newsBreakingList.size(), result.size());

        verify(newsBreakingRepository, times(1)).findAll();
    }

}