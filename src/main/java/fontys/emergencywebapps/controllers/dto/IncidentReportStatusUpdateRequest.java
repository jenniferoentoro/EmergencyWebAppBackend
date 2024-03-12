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

public class IncidentReportStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private String status;

    @NotNull(message = "Description is required")
    private String description;


}
