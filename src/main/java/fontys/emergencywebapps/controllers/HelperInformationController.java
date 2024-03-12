package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.business.HelperInformationUseCases;
import fontys.emergencywebapps.controllers.dto.HelperInformationDto;
import fontys.emergencywebapps.controllers.dto.HelperInformationResponse;
import fontys.emergencywebapps.persistence.entities.HelperInformation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helper-information")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelperInformationController {

    private final HelperInformationUseCases helperInformationService;

    private final ModelMapper modelMapper;
    @PostMapping
    public ResponseEntity<HelperInformationResponse> createHelperInformation(@Valid @RequestBody HelperInformationDto helperInformation) {
        HelperInformation helperInformation1 = helperInformationService.createHelperInformation(helperInformation);


        return ResponseEntity.ok(modelMapper.map(helperInformation1, HelperInformationResponse.class));
    }

    @PostMapping("/update")
    public ResponseEntity<HelperInformation> updateHelperInformation(@Valid @RequestBody HelperInformationDto helperInformation, HttpServletRequest request) {
        HelperInformation helperInformation1 = helperInformationService.updateHelperInformation(helperInformation, request);
        return ResponseEntity.ok(modelMapper.map(helperInformation1, HelperInformation.class));
    }

    @GetMapping("/get")
    public ResponseEntity<HelperInformation> getHelperInformation(@Valid @RequestBody Long id) {
        return ResponseEntity.ok(helperInformationService.getHelperInformation(id));
    }

    @GetMapping("/get-by-user")
    public ResponseEntity<HelperInformation> getHelperInformationByUser(@Valid @RequestBody Long id) {
        return ResponseEntity.ok(helperInformationService.getHelperInformationByUser(id));
    }

    @GetMapping
    public ResponseEntity<List<HelperInformation>> getAllHelperInformation() {
        return ResponseEntity.ok(helperInformationService.getAllHelperInformation());
    }
}
