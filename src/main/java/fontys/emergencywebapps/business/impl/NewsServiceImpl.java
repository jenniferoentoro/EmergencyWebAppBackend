package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.NewsUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.NewsByCategoriesRequest;
import fontys.emergencywebapps.controllers.dto.NewsRequest;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.News;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class NewsServiceImpl implements NewsUseCases {
    private final NewsRepository newsRepository;
    private final ModelMapper modelMapper;
    private final IncidentCategoryRepository incidentCategoryRepository;
    private static final String NOT_FOUND = "News not found";

    private static final String FILE_EXTENSION_NOT_VALID = "File extension is not valid";

    @Override
    public News getNewsById(Long id) {
        Optional<News> newsResponse = newsRepository.findById(id);

        if (newsResponse.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }

        return newsResponse.get();

    }

    @Override
    public Iterable<News> getNewsByTitle(String title) {
        return newsRepository.findByTitleContaining(title);
    }

    @Override
    public News createNews(NewsRequest request) throws IOException {
        if (request.getImage().getSize() > 3000000) {
            throw new CustomException("Image size is too large");
        }


        UUID uuid = UUID.randomUUID();
        String fileExtension = "";

        if (request.getImage() == null || request.getImage().getOriginalFilename() == null) {
            throw new CustomException(FILE_EXTENSION_NOT_VALID);
        }

        String originalFilename = request.getImage().getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            String[] splitFileName = originalFilename.split("\\.");
            if (splitFileName.length > 1) {
                fileExtension = splitFileName[1];
            }
        } else {
            throw new CustomException(FILE_EXTENSION_NOT_VALID);
        }
        String fileName = uuid.toString() + "." + fileExtension;
        String uploadDirPath = "D:\\Kuliah\\Fontys\\Semester3\\Java\\Project\\emergency-web-apps-frontend\\public\\" + "newsPhotos";
        File uploadDir = new File(uploadDirPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        request.getImage().transferTo(new File(uploadDirPath + File.separator + fileName));

//        check if incident category exists
        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(request.getIncidentCategoryId());
        if (incidentCategory.isEmpty()) {
            throw new CustomException("Incident category not found");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = dateFormat.parse(request.getDate());
        } catch (ParseException e) {
            throw new CustomException("Date format is not correct");
        }


        News news = News.builder()
                .description(request.getDescription())
                .title(request.getTitle())
                .image(fileName).incidentCategory(incidentCategory.get())
                .date(date)
                .build();

        return newsRepository.save(news);
    }

    @Override
    public News updateNews(Long id, NewsRequest request) throws IOException {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }

        UUID uuid = UUID.randomUUID();
        String fileExtension = "";

        if (request.getImage() == null || request.getImage().getOriginalFilename() == null) {
            throw new CustomException(FILE_EXTENSION_NOT_VALID);
        }

        String originalFilename = request.getImage().getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            String[] splitFileName = originalFilename.split("\\.");
            if (splitFileName.length > 1) {
                fileExtension = splitFileName[1];
            }
        } else {
            throw new CustomException(FILE_EXTENSION_NOT_VALID);
        }
        String fileName = uuid.toString() + "." + fileExtension;
        String uploadDirPath = "D:\\Kuliah\\Fontys\\Semester3\\Java\\Project\\emergency-web-apps-frontend\\public\\" + "newsPhotos";
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        request.getImage().transferTo(new File(uploadDirPath + File.separator + fileName));

        news.get().setTitle(request.getTitle());
        news.get().setDescription(request.getDescription());
        news.get().setImage(fileName);
        return newsRepository.save(news.get());
    }

    @Override
    public void deleteNews(Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        newsRepository.deleteById(id);
    }

    @Override
    public Iterable<News> getNews() {
        return newsRepository.findAll();

    }

    @Override
    public Iterable<News> getNewsByCategories(NewsByCategoriesRequest request) {
        List<String> categories = request.getCategories();
        List<News> news = new ArrayList<>();

        for (String category : categories) {
            news.addAll(newsRepository.findByIncidentCategory(incidentCategoryRepository.findByName(category)));
        }
        return news;
    }



}
