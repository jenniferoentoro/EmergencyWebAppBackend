package fontys.emergencywebapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorialDto {

        private Long id;

        private String title;

        private String description;

        private String linkToVideo;

        private String videoFile;

        private IncidentCategoryDto incidentCategory;
}
