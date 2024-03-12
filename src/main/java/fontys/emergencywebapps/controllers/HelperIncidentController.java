package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.business.HelpIncidentUseCases;
import fontys.emergencywebapps.controllers.dto.HelpIncidentAssignDto;
import fontys.emergencywebapps.controllers.dto.HelpIncidentDto;
import fontys.emergencywebapps.controllers.dto.HelpIncidentResponse;
import fontys.emergencywebapps.controllers.dto.LocationChange;
import fontys.emergencywebapps.persistence.entities.HelpIncident;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helper-incident")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelperIncidentController {
    private final HelpIncidentUseCases helpIncidentService;
    private final ModelMapper modelMapper;
    @PostMapping("/create/{username}")
    public ResponseEntity<HelpIncidentResponse> create(@Valid @RequestBody HelpIncidentDto helpIncident, @PathVariable String username) {
        HelpIncident helpIncident1 = helpIncidentService.createHelpIncident(helpIncident, username);
        return ResponseEntity.ok(modelMapper.map(helpIncident1, HelpIncidentResponse.class));
    }

    @PostMapping("/update-location/{id}")
    public ResponseEntity<HelpIncident> updateLocation(@Valid @RequestBody LocationChange helpIncident, @PathVariable Long id) {
        return ResponseEntity.ok(helpIncidentService.updateHelpIncidentLocation(helpIncident.getLatitude(), helpIncident.getLongitude(), id));
    }

    @PostMapping("/update-status/{id}")
    public ResponseEntity<HelpIncident> updateStatus(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(helpIncidentService.updateStatusDone(id));
    }

    @GetMapping
    public ResponseEntity<List<HelpIncident>> getAll() {
        return ResponseEntity.ok(helpIncidentService.getAllHelpIncidents());
    }

    @GetMapping("/get-by-user/{id}")
    public ResponseEntity<List<HelpIncident>> getByUser(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(helpIncidentService.getAllHelpIncidentsByUser(id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HelpIncident> get(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(helpIncidentService.getHelpIncident(id));
    }

    @GetMapping("/get-by-username/{username}")
    public ResponseEntity<String> getByUsername(@Valid @PathVariable String username) {
        return ResponseEntity.ok(helpIncidentService.getHelpIncidentByUsername(username));
    }

    @GetMapping("/get-by-helper")
    public ResponseEntity<HelpIncident> getByHelper(HttpServletRequest request) {
        return ResponseEntity.ok(helpIncidentService.findHelpIncidentByHelperIdAndStatus(request));
    }

    @PutMapping("/assign/{id}")
    public ResponseEntity<HelpIncident> assign(@Valid @PathVariable Long id, @RequestBody HelpIncidentAssignDto helpIncidentAssignDto, HttpServletRequest request) {
        return ResponseEntity.ok(helpIncidentService.assignHelper(id, helpIncidentAssignDto, request));
    }

    @GetMapping("/get-by-user")
    public ResponseEntity<HelpIncident> getByUser(HttpServletRequest request) {
        return ResponseEntity.ok(helpIncidentService.findHelpIncidentByUserIdAndStatus(request));
    }


}
