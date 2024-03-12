package fontys.emergencywebapps.controllers.dto;

import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelperInformationDto {
    @NotNull(message = "User is required")
    private Long userId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotNull(message = "Incident Category is required")
    private Long incidentCategoryId;
}