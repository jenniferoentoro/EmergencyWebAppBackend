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
public class TutorialRequest {
        @NotNull(message = "Title is required")
        private String title;

        @NotNull(message = "Description is required")
        private String description;

        private String linkToVideo;

        private MultipartFile videoFile;

        @NotNull(message = "Incident category is required")
        private Long incidentCategory;
}
