package fontys.emergencywebapps.controllers.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidentReportDiscussionDTO {
    private Long id;

    private String description;


    private UserDTO user;

    private Date date;

}
