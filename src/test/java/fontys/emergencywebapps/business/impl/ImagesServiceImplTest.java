package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.ImagesNewsDto;
import fontys.emergencywebapps.persistence.entities.ImagesNews;
import fontys.emergencywebapps.persistence.repos.ImagesNewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ImagesServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class ImagesServiceImplTest {

    @Mock
    private ImagesNewsRepository imagesNewsRepository;

    @InjectMocks
    private ImagesServiceImpl imagesService;

    @Test
    void saveImage_ValidImage_ShouldSaveImage() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("test-image.jpg", "test-image.jpg", "image/jpeg", "Test image content".getBytes());
        ImagesNewsDto imagesNewsDto = new ImagesNewsDto();
        imagesNewsDto.setFile(multipartFile);

        when(imagesNewsRepository.save(any(ImagesNews.class))).thenReturn(new ImagesNews());

        imagesService.saveImage(imagesNewsDto);

        ArgumentCaptor<ImagesNews> imagesNewsArgumentCaptor = ArgumentCaptor.forClass(ImagesNews.class);
        verify(imagesNewsRepository).save(imagesNewsArgumentCaptor.capture());
        ImagesNews imagesNews = imagesNewsArgumentCaptor.getValue();
        assertNotNull(imagesNews);
    }

    @Test
    void saveImage_LargeFileSize_ShouldThrowCustomException() {
        MultipartFile multipartFile = new MockMultipartFile("test-image.jpg", "test-image.jpg", "image/jpeg", new byte[4000000]);
        ImagesNewsDto imagesNewsDto = new ImagesNewsDto();
        imagesNewsDto.setFile(multipartFile);

        assertThrows(CustomException.class, () -> imagesService.saveImage(imagesNewsDto));
        verify(imagesNewsRepository, never()).save(any(ImagesNews.class));
    }

    @Test
    void saveImage_InvalidFileExtension_ShouldThrowCustomException() {
        MultipartFile multipartFile = new MockMultipartFile("test-image", "test-image", "image/jpeg", "Test image content".getBytes());
        ImagesNewsDto imagesNewsDto = new ImagesNewsDto();
        imagesNewsDto.setFile(multipartFile);
        assertThrows(CustomException.class, () -> imagesService.saveImage(imagesNewsDto));
        verify(imagesNewsRepository, never()).save(any(ImagesNews.class));
    }

}