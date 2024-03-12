package fontys.emergencywebapps.controllers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class HelpIncidentAssignDto {
    @NotNull(message = "Longitude is required")
    private String longitude;

    @NotNull(message = "Latitude is required")
    private String latitude;




}
