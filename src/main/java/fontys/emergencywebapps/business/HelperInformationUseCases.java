package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.HelperInformationDto;
import fontys.emergencywebapps.persistence.entities.HelperInformation;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface HelperInformationUseCases {
    HelperInformation createHelperInformation(HelperInformationDto helperInformation);

    HelperInformation updateHelperInformation(HelperInformationDto helperInformation, HttpServletRequest request);

    HelperInformation getHelperInformation(Long id);

    HelperInformation getHelperInformationByUser(Long id);

    List<HelperInformation> getAllHelperInformation();
}
