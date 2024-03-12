package fontys.emergencywebapps.controllers.dto;

import fontys.emergencywebapps.persistence.entities.Status;
import lombok.*;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidentReportStatusDTO {

    private Long id;

    private Status status;

    private String description;


    private Date date;
}
