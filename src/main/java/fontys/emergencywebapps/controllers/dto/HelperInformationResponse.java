package fontys.emergencywebapps.controllers.dto;

import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelperInformationResponse {
    private UserGetDTO user;
    private String companyName;
    private IncidentCategory incidentCategoryName;

}
