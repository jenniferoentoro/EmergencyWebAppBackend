package fontys.emergencywebapps.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentReportDtoResponse {

    private Long id;

    private String description;

    private String file;

    private Long latitude;

    private Long longitude;

    private IncidentCategory incidentCategory;

    private String title;

    private Boolean statusFixed;

    private UserDTO user;

    private Date date;


}
