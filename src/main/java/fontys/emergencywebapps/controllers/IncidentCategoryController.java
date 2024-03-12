package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.controllers.dto.IncidentCategoryDto;
import fontys.emergencywebapps.controllers.dto.IncidentCategoryRequest;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fontys.emergencywebapps.business.IncidentCategoryUseCases;
import fontys.emergencywebapps.controllers.dto.IncidentCategoriesDelete;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@RequestMapping("/api/incident-categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IncidentCategoryController {
    private final IncidentCategoryUseCases incidentCategoryService;


    private final ModelMapper modelMapper;

    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<IncidentCategoryDto> create(@Valid @RequestBody IncidentCategoryRequest request) {
        IncidentCategory incidentCategory = incidentCategoryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(incidentCategory, IncidentCategoryDto.class));
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping
    public ResponseEntity<Iterable<IncidentCategoryDto>> findAll() {

        Iterable<IncidentCategory> incidentCategories = incidentCategoryService.findAll();
        ArrayList<IncidentCategoryDto> dtos = new ArrayList<>();
        incidentCategories.forEach(
                incidentCategory -> {
                    IncidentCategoryDto dto = modelMapper.map(incidentCategory, IncidentCategoryDto.class);
                    dtos.add(dto);
                }
        );

        return ResponseEntity.ok().body(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/{id}")
    public ResponseEntity<IncidentCategoryDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(modelMapper.map(incidentCategoryService.findById(id), IncidentCategoryDto.class));
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<IncidentCategoryDto> update(@Valid @RequestBody IncidentCategoryRequest request, @PathVariable("id") Long id) {
        IncidentCategoryDto incidentCategoryDto = modelMapper.map(request, IncidentCategoryDto.class);
        incidentCategoryDto.setId(id);
        return ResponseEntity.ok().body(modelMapper.map(incidentCategoryService.update(incidentCategoryDto), IncidentCategoryDto.class));
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeOne(@PathVariable("id") Long id) {
        incidentCategoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @RolesAllowed("ADMIN")
    @DeleteMapping
    public ResponseEntity<String> removeMany(@RequestBody IncidentCategoriesDelete ids) {
        incidentCategoryService.deleteIncidentCategories(ids.getIds());
        return ResponseEntity.noContent().build();
    }
}
