package fontys.emergencywebapps.controllers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequest {

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    private String date;

    @NotNull(message = "Image is required")
    private MultipartFile image;

    @NotNull(message = "Incident Category is required")
    private Long incidentCategoryId;
}
