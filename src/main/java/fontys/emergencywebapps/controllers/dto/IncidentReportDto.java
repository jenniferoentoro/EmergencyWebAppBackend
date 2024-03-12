package fontys.emergencywebapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.User;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentReportDto {

    private Long id;

    private String description;

    private MultipartFile file;

    private Long latitude;

    private Long longitude;

    private IncidentCategory incidentCategory;

    private String title;

    private Boolean statusFixed;
    private User user;
    private Date date;


}
