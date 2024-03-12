package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.controllers.dto.IncidentReportStatusDTO;
import fontys.emergencywebapps.controllers.dto.IncidentReportStatusRequest;
import fontys.emergencywebapps.controllers.dto.IncidentReportStatusUpdateRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReportStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fontys.emergencywebapps.business.IncidentReportStatusUseCases;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/incident-report-statuses")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IncidentReportStatusController {
    private final IncidentReportStatusUseCases incidentReportStatusService;

    private final ModelMapper modelMapper;
    @PostMapping
    public ResponseEntity<IncidentReportStatusDTO> create(@Valid @RequestBody IncidentReportStatusRequest request) {
        IncidentReportStatus incidentReportStatus = incidentReportStatusService.createIncidentReportStatus(request);
        return ResponseEntity.ok().body(modelMapper.map(incidentReportStatus, IncidentReportStatusDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentReportStatusDTO> get(@PathVariable Long id) {
        IncidentReportStatus incidentReportStatus = incidentReportStatusService.getIncidentReportStatus(id);
        return ResponseEntity.ok().body(modelMapper.map(incidentReportStatus, IncidentReportStatusDTO.class));
    }

    @GetMapping("/incident-report/{id}")
    public ResponseEntity<Iterable<IncidentReportStatusDTO>> getByIncidentReportId(@PathVariable Long id) {
        Iterable<IncidentReportStatus> incidentReportStatuses = incidentReportStatusService.getByIncidentReportId(id);
        ArrayList<IncidentReportStatusDTO> dtos = new ArrayList<>();
        incidentReportStatuses.forEach(
                incidentReportStatus -> {
                    IncidentReportStatusDTO dto = modelMapper.map(incidentReportStatus, IncidentReportStatusDTO.class);
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok().body(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentReportStatusDTO> update(@Valid @RequestBody IncidentReportStatusUpdateRequest request, @PathVariable Long id) {
        return ResponseEntity.ok().body(modelMapper.map(incidentReportStatusService.updateIncidentReportStatus(request, id), IncidentReportStatusDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentReportStatusService.deleteIncidentReportStatus(id);
        return ResponseEntity.noContent().build();
    }
}
