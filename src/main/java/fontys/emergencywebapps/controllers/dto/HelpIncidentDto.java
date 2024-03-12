package fontys.emergencywebapps.controllers.dto;

import fontys.emergencywebapps.persistence.entities.ChatConnection;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.StatusHelpIncident;
import fontys.emergencywebapps.persistence.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class HelpIncidentDto {

    @NotNull(message = "Incident Category is required")
    private Long incidentCategoryId;



}
