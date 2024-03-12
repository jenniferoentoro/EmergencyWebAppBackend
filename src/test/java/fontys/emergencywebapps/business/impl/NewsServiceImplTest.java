package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.NewsByCategoriesRequest;
import fontys.emergencywebapps.controllers.dto.NewsRequest;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.News;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.NewsRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {NewsServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsServiceImpl newsUseCases;

    @Mock
    private IncidentCategoryRepository incidentCategoryRepository;


    @Test
    void testGetNewsById_ExistingId_ShouldReturnNews() {
        Long newsId = 1L;

        News expectedNews = News.builder().description("testDescription").title("testTitle").incidentCategory(IncidentCategory.builder().name("Category 1").build()).date(new Date()).image("https://image.com").build();
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(expectedNews));

        News result = newsUseCases.getNewsById(newsId);

        assertEquals(expectedNews, result);
    }

    @Test
    void testGetNewsById_NonExistingId_ShouldThrowNewsNotFoundException() {
        Long newsId = 1L;
        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsUseCases.getNewsById(newsId));
    }

    @Test
    void testGetNewsByTitle_ExistingTitle_ShouldReturnNewsList() {
        String title = "Test News";

        News expectedNews1 = News.builder().id(1L).title(title).description("Content 1").incidentCategory(IncidentCategory.builder().name("Category 1").build()).date(new Date()).image("https://image.com").build();
        News expectedNews2 = News.builder().id(2L).title(title).description("Content 2").incidentCategory(IncidentCategory.builder().name("Category 1").build()).date(new Date()).image("https://image.com").build();

        when(newsRepository.findByTitleContaining(title)).thenReturn(Arrays.asList(expectedNews1, expectedNews2));

        Iterable<News> result = newsUseCases.getNewsByTitle(title);

        assertNotNull(result);

        List<News> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(2, resultList.size(), "Size of the result list should be 2");

        assertEquals(expectedNews1.getId(), resultList.get(0).getId(), "Id should match for expectedNews1");
        assertEquals(expectedNews1.getTitle(), resultList.get(0).getTitle(), "Title should match for expectedNews1");
        assertEquals(expectedNews1.getDescription(), resultList.get(0).getDescription(), "Description should match for expectedNews1");

        assertEquals(expectedNews2.getId(), resultList.get(1).getId(), "Id should match for expectedNews2");
        assertEquals(expectedNews2.getTitle(), resultList.get(1).getTitle(), "Title should match for expectedNews2");
        assertEquals(expectedNews2.getDescription(), resultList.get(1).getDescription(), "Description should match for expectedNews2");
    }


    @Test
    void testGetNewsByTitle_NonExistingTitle_ShouldReturnEmptyList() {
        String nonExistingTitle = "Nonexistent News";
        when(newsRepository.findByTitleContaining(nonExistingTitle)).thenReturn(Arrays.asList());

        Iterable<News> result = newsUseCases.getNewsByTitle(nonExistingTitle);

        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void testDeleteNews_ExistingId_ShouldDeleteNews() {
        Long existingNewsId = 1L;
        News existingNews = News.builder().id(existingNewsId).title("Test News").description("Content 1").build();
        when(newsRepository.findById(existingNewsId)).thenReturn(Optional.of(existingNews));

        newsUseCases.deleteNews(existingNewsId);

        verify(newsRepository).deleteById(existingNewsId);
    }

    @Test
    void testDeleteNews_NonExistingId_ShouldThrowCustomException() {
        Long nonExistingNewsId = 1L;
        when(newsRepository.findById(nonExistingNewsId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsUseCases.deleteNews(nonExistingNewsId));
    }

    @Test
    void testGetNews_ShouldReturnNewsList() {
        News news1 = News.builder().id(1L).title("Test News 1").description("Content 1").incidentCategory(IncidentCategory.builder().name("Category 1").build()).date(new Date()).image("https://image.com").build();
        News news2 = News.builder().id(2L).title("Test News 2").description("Content 2").incidentCategory(IncidentCategory.builder().name("Category 1").build()).date(new Date()).image("https://image.com").build();

        when(newsRepository.findAll()).thenReturn(Arrays.asList(news1, news2));

        Iterable<News> result = newsUseCases.getNews();

        assertNotNull(result);

        List<News> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(2, resultList.size(), "Size of the result list should be 2");

        assertEquals(news1.getId(), resultList.get(0).getId(), "Id should match for news1");
        assertEquals(news1.getTitle(), resultList.get(0).getTitle(), "Title should match for news1");
        assertEquals(news1.getDescription(), resultList.get(0).getDescription(), "Description should match for news1");

        assertEquals(news2.getId(), resultList.get(1).getId(), "Id should match for news2");
        assertEquals(news2.getTitle(), resultList.get(1).getTitle(), "Title should match for news2");
        assertEquals(news2.getDescription(), resultList.get(1).getDescription(), "Description should match for news2");
    }

    @Test
    void testGetNewsByCategories_FoundNewsForAllCategories_ShouldReturnCombinedNewsList() {
        List<String> categories = Arrays.asList("Category1", "Category2");

        List<News> newsCategory1 = Arrays.asList(
                News.builder().id(1L).title("News 1").incidentCategory(IncidentCategory.builder().name("Category1").build()).build(),
                News.builder().id(2L).title("News 2").incidentCategory(IncidentCategory.builder().name("Category1").build()).build()
        );

        List<News> newsCategory2 = Arrays.asList(
                News.builder().id(3L).title("News 3").incidentCategory(IncidentCategory.builder().name("Category2").build()).build(),
                News.builder().id(4L).title("News 4").incidentCategory(IncidentCategory.builder().name("Category2").build()).build()
        );

        when(incidentCategoryRepository.findByName("Category1")).thenReturn(IncidentCategory.builder().name("Category1").build());
        when(incidentCategoryRepository.findByName("Category2")).thenReturn(IncidentCategory.builder().name("Category2").build());

        when(newsRepository.findByIncidentCategory(IncidentCategory.builder().name("Category1").build())).thenReturn(newsCategory1);
        when(newsRepository.findByIncidentCategory(IncidentCategory.builder().name("Category2").build())).thenReturn(newsCategory2);

        // Act
        Iterable<News> result = newsUseCases.getNewsByCategories(NewsByCategoriesRequest.builder().categories(categories).build());

        assertNotNull(result);

        List<News> resultList = new ArrayList<>();
        result.forEach(resultList::add);

        assertEquals(4, resultList.size(), "Size of the result list should be 4");

    }

    @Test
    void testCreateNews_ImageSizeTooLarge_ShouldThrowCustomException() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[3500000]);

        NewsRequest request = NewsRequest.builder()
                .title("Test News")
                .description("Content 1")
                .image(image)
                .incidentCategoryId(1L)
                .build();

        assertThrows(CustomException.class, () -> newsUseCases.createNews(request), "Image size is too large");
    }

    @Test
    void testCreateNews_FileExtensionNotValid_ShouldThrowCustomException() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", "test", "image", "valid image content".getBytes());
        NewsRequest request = NewsRequest.builder().title("Test News").description("Content 1").image(image).incidentCategoryId(1l).build();
        assertThrows(CustomException.class, () -> newsUseCases.createNews(request), "File extension is not valid");
    }

    @Test
    void testCreateNews_UploadDirNotExists_ShouldCreateDirectory() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "valid image content".getBytes());
        NewsRequest request = NewsRequest.builder().title("Test News").description("Content 1").image(image).incidentCategoryId(1L).build();
        File existingUploadDir = new File("D:\\Kuliah\\Fontys\\Semester3\\Java\\Project\\emergency-web-apps-frontend\\public\\newsPhotos");
        FileUtils.deleteDirectory(existingUploadDir);

        assertThrows(CustomException.class, () -> newsUseCases.createNews(request), "Incident category not found");
    }

    @Test
    void testCreateNews_InvalidDateFormat_ShouldThrowCustomException() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "valid image content".getBytes());
        NewsRequest request = NewsRequest.builder().title("Test News").description("Content 1").image(image).incidentCategoryId(1L).date("invalidDate").build();

        assertThrows(CustomException.class, () -> newsUseCases.createNews(request), "Date format is not correct");
    }

    @Test
    void testCreateNews_NullImage_ShouldThrowCustomException() {
        NewsRequest request = NewsRequest.builder()
                .title("Test News")
                .description("Content 1")
                .image(new MockMultipartFile("image", "test.jpg", "image/jpeg", "valid image content".getBytes()))
                .incidentCategoryId(1L)
                .build();

        assertThrows(CustomException.class, () -> newsUseCases.createNews(request), "Image is null");
    }



    @Test
    void testCreateNews_NullOriginalFilename_ShouldThrowCustomException() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", null, "image/jpeg", "valid image content".getBytes());

        NewsRequest request = NewsRequest.builder()
                .title("Test News")
                .description("Content 1")
                .image(image)
                .incidentCategoryId(1L)
                .build();

        assertThrows(CustomException.class, () -> newsUseCases.createNews(request), "File extension is not valid");
    }

    @Test
    void testCreateNews_IncidentCategoryNotFound_ShouldThrowCustomException() throws IOException {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "valid image content".getBytes());
        NewsRequest request = NewsRequest.builder().title("Test News").description("Content 1").image(image).incidentCategoryId(1l).build();
        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsUseCases.createNews(request), "Incident category not found");
    }

    @Test
    void testCreateNews_SuccessfulCreation_ShouldReturnNews() throws IOException, ParseException {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "valid image content".getBytes());
        NewsRequest request = NewsRequest.builder().title("Test News").description("Content 1").image(image).incidentCategoryId(1L).date("2021-01-01").build();

        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.of(new IncidentCategory()));
        when(newsRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        News result = newsUseCases.createNews(request);

        assertNotNull(result);
    }

    @Test
    void testUpdateNews_NewsFound_ShouldReturnUpdatedNews() throws IOException {
        Long newsId = 1L;
        NewsRequest request = createNewsRequest("Updated Title", "Updated Description", "updated-image.jpg");

        News existingNews = createNews(newsId, "Original Title", "Original Description", "original-image.jpg");
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        News result = newsUseCases.updateNews(newsId, request);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testUpdateNews_NewsNotFound_ShouldThrowCustomException() throws IOException {
        Long nonExistingNewsId = 999L;
        NewsRequest request = createNewsRequest("Updated Title", "Updated Description", "updated-image.jpg");

        when(newsRepository.findById(nonExistingNewsId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsUseCases.updateNews(nonExistingNewsId, request), "News not found");
    }

    @Test
    void testUpdateNews_FileExtensionNotValid_ShouldThrowCustomException() throws IOException {
        // Arrange
        Long newsId = 1L;
        NewsRequest request = createNewsRequest("Updated Title", "Updated Description", "updated-image");

        News existingNews = createNews(newsId, "Original Title", "Original Description", "original-image.jpg");
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));

        // Act and Assert
        assertThrows(CustomException.class, () -> newsUseCases.updateNews(newsId, request), "File extension is not valid");
    }

    private NewsRequest createNewsRequest(String title, String description, String imageName) {
        MockMultipartFile image = new MockMultipartFile("image", imageName, "image/jpeg", "valid image content".getBytes());
        return NewsRequest.builder().title(title).description(description).image(image).build();
    }

    private News createNews(Long id, String title, String description, String imageName) {
        return News.builder().id(id).title(title).description(description).image(imageName).build();
    }


}