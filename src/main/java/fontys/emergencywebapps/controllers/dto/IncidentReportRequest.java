package fontys.emergencywebapps.controllers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidentReportRequest {
    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "File is required")
    private MultipartFile file;

    @NotNull(message = "Location is required")
    private Double latitude;

    @NotNull(message = "Location is required")
    private Double longitude;

    @NotNull(message = "Category is required")
    private Long incidentCategoryId;

    @NotNull(message = "Title is required")
    private String title;

}
