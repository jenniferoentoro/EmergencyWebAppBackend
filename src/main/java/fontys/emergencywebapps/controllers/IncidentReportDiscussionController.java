package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionUpdateRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReportDiscussion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fontys.emergencywebapps.business.IncidentDiscussionUseCases;
import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionRequest;
import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionDTO;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/incident-report-discussions")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IncidentReportDiscussionController {
    private final IncidentDiscussionUseCases incidentReportDiscussionService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<IncidentReportDiscussionDTO> create(@Valid @RequestBody IncidentReportDiscussionRequest request, HttpServletRequest servletRequest) {
        IncidentReportDiscussion incidentReportDiscussion = incidentReportDiscussionService.createIncidentReportDiscussion(request, servletRequest);
        return ResponseEntity.ok().body(modelMapper.map(incidentReportDiscussion, IncidentReportDiscussionDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentReportDiscussionDTO> update(@Valid @RequestBody IncidentReportDiscussionUpdateRequest request, @PathVariable Long id, HttpServletRequest servletRequest) {
        IncidentReportDiscussion incidentReportDiscussion = incidentReportDiscussionService.updateIncidentReportDiscussion(id, request, servletRequest);
        return ResponseEntity.ok().body(modelMapper.map(incidentReportDiscussion, IncidentReportDiscussionDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentReportDiscussionDTO> get(@PathVariable Long id) {
        IncidentReportDiscussion incidentReportDiscussion = incidentReportDiscussionService.getIncidentReportDiscussion(id);
        return ResponseEntity.ok().body(modelMapper.map(incidentReportDiscussion, IncidentReportDiscussionDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentReportDiscussionService.deleteIncidentReportDiscussion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Iterable<IncidentReportDiscussionDTO>> getAll() {
        Iterable<IncidentReportDiscussion> incidentReportDiscussions = incidentReportDiscussionService.getAllIncidentReportDiscussion();
        ArrayList<IncidentReportDiscussionDTO> dtos = new ArrayList<>();
        incidentReportDiscussions.forEach(
                incidentReportDiscussion -> {
                    IncidentReportDiscussionDTO dto = modelMapper.map(incidentReportDiscussion, IncidentReportDiscussionDTO.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/incident-report/{id}")
    public ResponseEntity<Iterable<IncidentReportDiscussionDTO>> getByIncidentReportId(@PathVariable Long id) {
        Iterable<IncidentReportDiscussion> incidentReportDiscussions = incidentReportDiscussionService.getAllIncidentReportDiscussionByIncidentReportId(id);
        ArrayList<IncidentReportDiscussionDTO> dtos = new ArrayList<>();
        incidentReportDiscussions.forEach(
                incidentReportDiscussion -> {
                    IncidentReportDiscussionDTO dto = modelMapper.map(incidentReportDiscussion, IncidentReportDiscussionDTO.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok().body(dtos);
    }
}
