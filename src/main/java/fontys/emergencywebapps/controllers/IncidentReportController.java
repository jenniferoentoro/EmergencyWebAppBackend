package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.controllers.dto.IncidentReportDtoResponse;
import fontys.emergencywebapps.controllers.dto.IncidentReportRequest;
import fontys.emergencywebapps.controllers.dto.UserDTO;
import fontys.emergencywebapps.persistence.entities.IncidentReport;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fontys.emergencywebapps.business.IncidentReportUseCases;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/incident-reports")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class IncidentReportController {
    private final IncidentReportUseCases incidentReportService;

    private final ModelMapper modelMapper;

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/me")
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findAllByCurrentUser(HttpServletRequest servletRequest) {
        Iterable<IncidentReport> incidentReports = incidentReportService.findAllByMe(servletRequest);
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @PostMapping
    public ResponseEntity<IncidentReportDtoResponse> uploadPhoto(@Valid @ModelAttribute IncidentReportRequest request, HttpServletRequest servletRequest) throws IOException {
        IncidentReport incidentReport = incidentReportService.save(request, servletRequest);
        IncidentReportDtoResponse result = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
        result.setFile(incidentReport.getPhotoUrl());
        result.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("{id}")
    public ResponseEntity<IncidentReportDtoResponse> findById(@PathVariable(value = "id") final long id) {
        IncidentReport incidentReport = incidentReportService.findOne(id);
        IncidentReportDtoResponse result = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
        result.setFile(incidentReport.getPhotoUrl());
        result.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
        return ResponseEntity.ok(result);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findAll() {
        Iterable<IncidentReport> incidentReports = incidentReportService.findAll();
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/user/{id}")
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findAllByUserId(@PathVariable(value = "id") final long id) {
        Iterable<IncidentReport> incidentReports = incidentReportService.findAllByUserId(id);
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/category/{id}")
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findAllByIncidentCategoryId(@PathVariable(value = "id") final long id) {
        Iterable<IncidentReport> incidentReports = incidentReportService.findAllByIncidentCategoryId(id);
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/category/{incidentCategoryId}/user/{userId}")
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findAllByIncidentCategoryIdAndUserId(@PathVariable(value = "incidentCategoryId") final long incidentCategoryId, @PathVariable(value = "userId") final long userId) {
        Iterable<IncidentReport> incidentReports = incidentReportService.findAllByIncidentCategoryIdAndUserId(incidentCategoryId, userId);
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/in-progress")
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findInProgressReports() {
        Iterable<IncidentReport> incidentReports = incidentReportService.findInProgressReports();
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/fixed")
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findFixedReports() {
        Iterable<IncidentReport> incidentReports = incidentReportService.findFixedReports();
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/waiting")
    public ResponseEntity<Iterable<IncidentReportDtoResponse>> findWaitingReports() {
        Iterable<IncidentReport> incidentReports = incidentReportService.findWaitingReports();
        ArrayList<IncidentReportDtoResponse> dtos = new ArrayList<>();
        incidentReports.forEach(
                incidentReport -> {
                    IncidentReportDtoResponse dto = modelMapper.map(incidentReport, IncidentReportDtoResponse.class);
                    dto.setFile(incidentReport.getPhotoUrl());
                    dto.setUser(UserDTO.builder().username(incidentReport.getUser().getUsername()).id(incidentReport.getUser().getId()).build());
                    dtos.add(dto);
                }
        );
        return ResponseEntity.ok(dtos);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/in-progress/count")
    public ResponseEntity<Integer> countInProgressReports() {
        return ResponseEntity.ok(incidentReportService.countInProgressReports());
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/fixed/count")
    public ResponseEntity<Integer> countFixedReports() {
        return ResponseEntity.ok(incidentReportService.countFixedReports());
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/waiting/count")
    public ResponseEntity<Integer> countWaitingReports() {
        return ResponseEntity.ok(incidentReportService.countWaitingReports());
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/status-fixed/count")
    public ResponseEntity<Integer> countStatusFixed() {
        return ResponseEntity.ok(incidentReportService.countStatusFixed());
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/status-in-progress/count")
    public ResponseEntity<Integer> countStatusInProgress() {
        return ResponseEntity.ok(incidentReportService.countStatusInProgress());
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/category/count")
    public ResponseEntity<Iterable<Object[]>> countIncidentReportsByCategory() {
        return ResponseEntity.ok(incidentReportService.countIncidentReportsByCategory());
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/last-5-days/count")
    public ResponseEntity<Iterable<Object[]>> countIncidentReportsLast5Days() {
        return ResponseEntity.ok(incidentReportService.countIncidentReportsLast5Days());
    }

}
