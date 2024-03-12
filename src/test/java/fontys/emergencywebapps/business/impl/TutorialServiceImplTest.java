package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.TutorialRequest;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.Tutorial;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.TutorialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TutorialServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class TutorialServiceImplTest {
    @Mock
    private IncidentCategoryRepository incidentCategoryRepository;

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialServiceImpl tutorialService;

    @Test
    void testCreateTutorial_Success() throws IOException {
        // Arrange
        TutorialRequest request = new TutorialRequest();
        request.setTitle("Test Title");
        request.setDescription("Test Description");
        request.setIncidentCategory(1L);
        request.setLinkToVideo("https://www.youtube.com/test");
        MockMultipartFile videoFile = new MockMultipartFile(
                "videoFile", "testVideo.mp4", "video/mp4", "testVideoData".getBytes()
        );
        request.setVideoFile(videoFile);

        IncidentCategory mockIncidentCategory = new IncidentCategory();
        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.of(mockIncidentCategory));

        Tutorial mockTutorial = new Tutorial();
        when(tutorialRepository.save(any())).thenReturn(mockTutorial);

        Tutorial result = tutorialService.createTutorial(request);

        assertNotNull(result);
        assertEquals(mockTutorial, result);
        verify(incidentCategoryRepository, times(1)).findById(1L);
        verify(tutorialRepository, times(1)).save(any());
    }

    @Test
    void testCreateTutorial_FileSizeTooLarge_Exception() {
        TutorialRequest request = new TutorialRequest();
        MockMultipartFile videoFile = new MockMultipartFile(
                "videoFile", "largeVideo.mp4", "video/mp4", new byte[4000000]
        );
        request.setVideoFile(videoFile);

        assertThrows(CustomException.class, () -> tutorialService.createTutorial(request));

    }

    @Test
    void testCreateTutorial_FileExtensionNotValid_Exception() {
        TutorialRequest request = new TutorialRequest();
        MockMultipartFile videoFile = new MockMultipartFile(
                "videoFile", "invalidVideo", null, "invalidVideoData".getBytes()
        );
        request.setVideoFile(videoFile);

        assertThrows(CustomException.class, () -> tutorialService.createTutorial(request));
    }

    @Test
    void testCreateTutorial_IncidentCategoryNotFound_Exception() {
        TutorialRequest request = new TutorialRequest();
        request.setIncidentCategory(1L);

        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> tutorialService.createTutorial(request));
    }

    @Test
    void testUpdateTutorial_TutorialNotFound_Exception() {
        Long tutorialId = 1L;
        TutorialRequest request = new TutorialRequest();

        when(tutorialRepository.findById(tutorialId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> tutorialService.updateTutorial(tutorialId, request));
    }

    @Test
    void testDeleteTutorial_Success() {
        Long tutorialId = 1L;
        Tutorial mockTutorial = new Tutorial();
        when(tutorialRepository.findById(tutorialId)).thenReturn(Optional.of(mockTutorial));

        assertDoesNotThrow(() -> tutorialService.deleteTutorial(tutorialId));

        verify(tutorialRepository, times(1)).findById(tutorialId);
        verify(tutorialRepository, times(1)).deleteById(tutorialId);
    }

    @Test
    void testDeleteTutorial_NotFound() {
        Long tutorialId = 1L;
        when(tutorialRepository.findById(tutorialId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> tutorialService.deleteTutorial(tutorialId));


        verify(tutorialRepository, times(1)).findById(tutorialId);
        verify(tutorialRepository, never()).deleteById(tutorialId);
    }

    @Test
    void testGetTutorial_Success() {
        Long tutorialId = 1L;
        Tutorial mockTutorial = new Tutorial();
        when(tutorialRepository.findById(tutorialId)).thenReturn(Optional.of(mockTutorial));

        Tutorial result = tutorialService.getTutorial(tutorialId);

        assertNotNull(result);
        assertEquals(mockTutorial, result);

        verify(tutorialRepository, times(1)).findById(tutorialId);
    }

    @Test
    void testGetTutorial_NotFound() {
        Long tutorialId = 1L;
        when(tutorialRepository.findById(tutorialId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> tutorialService.getTutorial(tutorialId));


        verify(tutorialRepository, times(1)).findById(tutorialId);
    }

    @Test
    void testGetTutorials() {
        List<Tutorial> mockTutorials = new ArrayList<>();
        when(tutorialRepository.findAll()).thenReturn(mockTutorials);

        Iterable<Tutorial> result = tutorialService.getTutorials();

        assertNotNull(result);
        assertEquals(mockTutorials, result);

        verify(tutorialRepository, times(1)).findAll();
    }

    @Test
    void testGetTutorialByTitle() {
        String tutorialTitle = "TutorialTitle";
        List<Tutorial> mockTutorials = new ArrayList<>();
        when(tutorialRepository.findAllByTitleContaining(tutorialTitle)).thenReturn(mockTutorials);

        Iterable<Tutorial> result = tutorialService.getTutorialByTitle(tutorialTitle);

        assertNotNull(result);
        assertEquals(mockTutorials, result);

        verify(tutorialRepository, times(1)).findAllByTitleContaining(tutorialTitle);
    }

    @Test
    void testSaveVideoFile_Success() throws IOException {
        MockMultipartFile videoFile = new MockMultipartFile(
                "videoFile", "video.mp4", "video/mp4", "videoData".getBytes()
        );

        String result = tutorialService.saveVideoFile(videoFile);

        assertNotNull(result);

    }

    @Test
    void testSaveVideoFile_InvalidExtension() {
        MockMultipartFile videoFile = new MockMultipartFile(
                "videoFile", "video", "video", "videoData".getBytes()
        );

        assertThrows(CustomException.class,
                () -> tutorialService.saveVideoFile(videoFile));

    }

    @Test
    void testHandleVideoFile_Success() throws IOException {
        TutorialRequest request = new TutorialRequest();
        MockMultipartFile videoFile = new MockMultipartFile(
                "videoFile", "video.mp4", "video/mp4", "videoData".getBytes()
        );
        request.setVideoFile(videoFile);

        String result = tutorialService.handleVideoFile(request);

        assertNotNull(result);
        assertTrue(result.endsWith(".mp4"));
    }


    @Test
    void testHandleVideoFile_NoFile() throws IOException {
        TutorialRequest request = new TutorialRequest();

        String result = tutorialService.handleVideoFile(request);

        assertNull(result);
    }

    @Test
    void testGetIncidentCategory_CategoryFound() {
        TutorialRequest request = new TutorialRequest();
        request.setIncidentCategory(1L);

        IncidentCategory mockCategory = new IncidentCategory();
        mockCategory.setId(1L);
        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        IncidentCategory result = tutorialService.getIncidentCategory(request);

        assertNotNull(result);
        assertEquals(mockCategory, result);
    }

    @Test
    void testGetIncidentCategory_CategoryNotFound() {
        TutorialRequest request = new TutorialRequest();
        request.setIncidentCategory(1L);

        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,
                () -> tutorialService.getIncidentCategory(request));

    }

}