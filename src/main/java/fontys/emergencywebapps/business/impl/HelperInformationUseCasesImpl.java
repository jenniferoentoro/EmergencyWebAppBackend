package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.HelperInformationUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.controllers.dto.HelperInformationDto;
import fontys.emergencywebapps.persistence.entities.HelperInformation;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.Role;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.HelperInformationRepository;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class HelperInformationUseCasesImpl implements HelperInformationUseCases {
    private final HelperInformationRepository helperInformationRepository;
    private final AccessTokenDecoder jwtService;
    private final UserRepository userRepository;
    private final IncidentCategoryRepository incidentCategoryRepository;

    @Override
    public HelperInformation createHelperInformation(HelperInformationDto helperInformation) {

        Optional<User> user = userRepository.findById(helperInformation.getUserId());
        if (user.isEmpty()) {
            throw new CustomException("User not found");
        }

//        if(!user.get().getRoles().contains(Role.HELPER)){
//            throw new CustomException("User is not a helper");
//        }

        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(helperInformation.getIncidentCategoryId());
        if (incidentCategory.isEmpty()) {
            throw new CustomException("Incident Category not found");
        }

        return helperInformationRepository.save(HelperInformation.builder()
                .companyName(helperInformation.getCompanyName())
                .incidentCategory(incidentCategory.get())
                .user(user.get())
                .build());
    }

    @Override
    public HelperInformation updateHelperInformation(HelperInformationDto helperInformation, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Optional<HelperInformation> helperInformation1 = helperInformationRepository.findByUserId(claims.getUserId());

        if (helperInformation1.isEmpty()) {
            throw new CustomException("Helper Information not found");
        }

        helperInformation1.get().setCompanyName(helperInformation.getCompanyName());
        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(helperInformation.getIncidentCategoryId());
        if (incidentCategory.isEmpty()) {
            throw new CustomException("Incident Category not found");
        }

        helperInformation1.get().setIncidentCategory(incidentCategory.get());

        return helperInformationRepository.save(helperInformation1.get());

    }

    @Override
    public HelperInformation getHelperInformation(Long id) {

        Optional<HelperInformation> helperInformation = helperInformationRepository.findById(id);

        if (helperInformation.isEmpty()) {
            throw new CustomException("Helper Information not found");
        }

        return helperInformation.get();
    }

    @Override
    public HelperInformation getHelperInformationByUser(Long id) {
        Optional<HelperInformation> helperInformation = helperInformationRepository.findByUserId(id);

        if (helperInformation.isEmpty()) {
            throw new CustomException("Helper Information not found");
        }

        return helperInformation.get();
    }

    @Override
    public List<HelperInformation> getAllHelperInformation() {
        return helperInformationRepository.findAll();
    }
}
