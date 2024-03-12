package fontys.emergencywebapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {

        private Long id;

        private String title;

        private String description;

        private Date date;

        private String image;

        private IncidentCategoryDto incidentCategory;
}
