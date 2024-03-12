package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.HelpIncidentAssignDto;
import fontys.emergencywebapps.controllers.dto.HelpIncidentDto;
import fontys.emergencywebapps.persistence.entities.HelpIncident;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface HelpIncidentUseCases {


    HelpIncident createHelpIncident(HelpIncidentDto helpIncident, String username);

    HelpIncident updateHelpIncidentLocation(Double latitude, Double longitude, Long id);

    HelpIncident updateStatusDone(Long id);


    List<HelpIncident> getAllHelpIncidents();

    List<HelpIncident> getAllHelpIncidentsByUser(Long id);

    HelpIncident getHelpIncident(Long id);

    HelpIncident assignHelper(Long id, HelpIncidentAssignDto helpIncidentAssignDto, HttpServletRequest request);

    String getHelpIncidentByUsername(String username);

    HelpIncident findHelpIncidentByHelperIdAndStatus(HttpServletRequest request);

    HelpIncident findHelpIncidentByUserIdAndStatus(HttpServletRequest request);

}
