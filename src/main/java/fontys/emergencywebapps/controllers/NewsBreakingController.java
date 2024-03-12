package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.controllers.dto.NewsBreakingResponse;
import fontys.emergencywebapps.persistence.entities.NewsBreaking;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fontys.emergencywebapps.business.NewsBreakingUseCases;
import fontys.emergencywebapps.controllers.dto.NewsBreakingRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/news-breaking")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsBreakingController {
    private final NewsBreakingUseCases newsBreakingUseCases;

    @PostMapping
    public ResponseEntity<NewsBreakingResponse> addNewsBreaking(@Valid @RequestBody NewsBreakingRequest newsBreakingRequest) {
        NewsBreaking newsBreaking = newsBreakingUseCases.addNewsBreaking(newsBreakingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body( NewsBreakingResponse.builder().newsId(newsBreakingRequest.getNewsId()).title(newsBreakingRequest.getTitle()).id(newsBreaking.getId()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeNewsHighlight(@PathVariable Long id) {
        newsBreakingUseCases.removeNewsHighlight(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<NewsBreakingResponse>> getNewsBreaking() {
        List<NewsBreaking> newsBreakings=  newsBreakingUseCases.getNewsBreaking();
        List<NewsBreakingResponse> dtos = new ArrayList<>();
        newsBreakings.forEach(
                newsEach -> {
                    NewsBreakingResponse dto = NewsBreakingResponse.builder().id(newsEach.getId()).newsId(newsEach.getNews().getId()).title(newsEach.getTitle()).build();
                    dtos.add(dto);
                }
        );
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }


}
