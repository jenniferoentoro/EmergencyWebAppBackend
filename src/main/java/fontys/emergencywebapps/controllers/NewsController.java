package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.controllers.dto.ImagesNewsDto;
import fontys.emergencywebapps.controllers.dto.NewsRequest;
import fontys.emergencywebapps.persistence.entities.News;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fontys.emergencywebapps.business.ImagesNewsUseCases;
import fontys.emergencywebapps.business.NewsUseCases;
import fontys.emergencywebapps.controllers.dto.NewsByCategoriesRequest;
import fontys.emergencywebapps.controllers.dto.NewsDTO;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/news")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {
    private final NewsUseCases newsUseCases;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<NewsDTO> uploadPhoto(@Valid @ModelAttribute NewsRequest request) throws IOException {
        News news = newsUseCases.createNews(request);
        NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);
        newsDTO.setImage(news.getImage());
        return ResponseEntity.status(HttpStatus.CREATED).body(newsDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<NewsDTO> findById(@PathVariable(value = "id") final long id) {
        return ResponseEntity.ok(modelMapper.map(newsUseCases.getNewsById(id), NewsDTO.class));
    }

    @GetMapping
    public ResponseEntity<Iterable<NewsDTO>> findAll() {
        Iterable<News> news = newsUseCases.getNews();
        ArrayList<NewsDTO> dtos = new ArrayList<>();
        news.forEach(
                newsEach -> {
                    NewsDTO dto = modelMapper.map(newsEach, NewsDTO.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Iterable<NewsDTO>> findByTitle(@PathVariable(value = "title") final String title) {
        Iterable<News> news = newsUseCases.getNewsByTitle(title);
        ArrayList<NewsDTO> dtos = new ArrayList<>();
        news.forEach(
                newsEach -> {
                    NewsDTO dto = modelMapper.map(newsEach, NewsDTO.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> update(@Valid @ModelAttribute NewsRequest request, @PathVariable Long id) throws IOException {
        News news = newsUseCases.updateNews(id, request);
        NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);
        newsDTO.setImage(news.getImage());
        return ResponseEntity.ok(newsDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        newsUseCases.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    private final ImagesNewsUseCases imagesNewsUseCases;

    @PostMapping("/images")
    public ResponseEntity<String> uploadPhoto(@Valid @ModelAttribute ImagesNewsDto request) throws IOException {
        imagesNewsUseCases.saveImage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PostMapping("/news-by-categories")
    public ResponseEntity<Iterable<NewsDTO>> findByCategories(@RequestBody NewsByCategoriesRequest list) {
        Iterable<News> news = newsUseCases.getNewsByCategories(list);
        ArrayList<NewsDTO> dtos = new ArrayList<>();
        news.forEach(
                newsEach -> {
                    NewsDTO dto = modelMapper.map(newsEach, NewsDTO.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

}
