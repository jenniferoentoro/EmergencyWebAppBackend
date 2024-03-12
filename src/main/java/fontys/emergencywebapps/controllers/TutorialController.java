package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.business.TutorialUseCases;
import fontys.emergencywebapps.controllers.dto.TutorialDto;
import fontys.emergencywebapps.controllers.dto.TutorialRequest;
import fontys.emergencywebapps.persistence.entities.Tutorial;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/tutorial")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TutorialController {
    private final TutorialUseCases tutorialService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TutorialDto> createTutorial(@Valid TutorialRequest tutorialDto) throws IOException {
        Tutorial tutorial = tutorialService.createTutorial(tutorialDto);
        TutorialDto tutorialDto1 = modelMapper.map(tutorial, TutorialDto.class);
        tutorialDto1.setVideoFile(tutorial.getVideoFile());
        return ResponseEntity.ok(tutorialDto1);
    }

    @GetMapping
    public ResponseEntity<Iterable<TutorialDto>> getTutorials() {
        Iterable<Tutorial> tutorials = tutorialService.getTutorials();
        ArrayList<TutorialDto> dtos = new ArrayList<>();
        tutorials.forEach(
                tutorialEach -> {
                    TutorialDto dto = modelMapper.map(tutorialEach, TutorialDto.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorialDto> getTutorial(@PathVariable(value = "id") Long id) {
        Tutorial tutorial = tutorialService.getTutorial(id);
        return ResponseEntity.ok(modelMapper.map(tutorial, TutorialDto.class));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Iterable<TutorialDto>> getTutorialByTitle(@PathVariable(value = "title") String title) {
        Iterable<Tutorial> tutorials = tutorialService.getTutorialByTitle(title);
        ArrayList<TutorialDto> dtos = new ArrayList<>();
        tutorials.forEach(
                tutorialEach -> {
                    TutorialDto dto = modelMapper.map(tutorialEach, TutorialDto.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TutorialDto> updateTutorial(@PathVariable(value = "id") Long id, @Valid TutorialRequest tutorialDto) throws IOException {
        Tutorial tutorial = tutorialService.updateTutorial(id, tutorialDto);
        TutorialDto tutorialDto1 = modelMapper.map(tutorial, TutorialDto.class);
        tutorialDto1.setVideoFile(tutorial.getVideoFile());
        return ResponseEntity.ok(tutorialDto1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable(value = "id") Long id) {
        tutorialService.deleteTutorial(id);
        return ResponseEntity.noContent().build();
    }
}
