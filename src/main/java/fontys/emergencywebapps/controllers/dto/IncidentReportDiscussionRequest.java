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

public class IncidentReportDiscussionRequest {


    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Incident Report id is required")
    private Long incidentReportId;

}
