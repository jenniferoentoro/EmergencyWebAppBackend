package fontys.emergencywebapps.controllers.dto;

import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class HelpIncidentResponse {

    private Long id;
    private Long chatConnectionId;

    private IncidentCategory incidentCategory;

    private String usernameUser;


    private String longitudeUser;



    private String latitudeUser;





}
