package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.business.NewsHighlightUseCases;
import fontys.emergencywebapps.controllers.dto.NewsDTO;
import fontys.emergencywebapps.controllers.dto.NewsHighlightRequest;
import fontys.emergencywebapps.persistence.entities.NewsHighlight;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/news-highlight")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsHiglightController {
    private final NewsHighlightUseCases newsHighlightUseCases;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<NewsDTO> addNewsHighlight(@Valid @RequestBody NewsHighlightRequest newsHighlightRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(newsHighlightUseCases.addNewsHighlight(newsHighlightRequest.getNewsId()), NewsDTO.class));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeNewsHighlight(@PathVariable Long id) {
        newsHighlightUseCases.removeNewsHighlight(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<NewsDTO>> getNewsHighlight() {
        List<NewsHighlight> newsHighlights = newsHighlightUseCases.getNewsHighlight();
        ArrayList<NewsDTO> dtos = new ArrayList<>();
        newsHighlights.forEach(
                newsEach -> {
                    NewsDTO dto = modelMapper.map(newsEach.getNews(), NewsDTO.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
