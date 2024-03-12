package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.HelpIncidentUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.controllers.dto.HelpIncidentAssignDto;
import fontys.emergencywebapps.controllers.dto.HelpIncidentDto;
import fontys.emergencywebapps.persistence.entities.*;
import fontys.emergencywebapps.persistence.repos.ChatConnectionRepository;
import fontys.emergencywebapps.persistence.repos.HelpIncidentRepository;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class HelperIncidentUseCasesImpl implements HelpIncidentUseCases {

    private final HelpIncidentRepository helpIncidentRepository;
    private final ChatConnectionRepository chatConnectionRepository;
    private final IncidentCategoryRepository incidentCategoryRepository;
    private final UserRepository userRepository;
    private final AccessTokenDecoder jwtService;


    @Override
    public HelpIncident createHelpIncident(HelpIncidentDto helpIncident, String username) {

        ChatConnection chatConnection = chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING);
        if (chatConnection == null) {
            throw new CustomException("ChatConnection not found");
        }

        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(helpIncident.getIncidentCategoryId());

        if (incidentCategory.isEmpty()) {
            throw new CustomException("IncidentCategory not found");
        }
        HelpIncident helpIncident1 = HelpIncident.builder()
                .chatConnection(chatConnection)
                .incidentCategory(incidentCategory.get())
                .statusHelpIncident(StatusHelpIncident.OPEN)
                .build();
        return helpIncidentRepository.save(helpIncident1);
    }

    @Override
    public HelpIncident updateHelpIncidentLocation(Double latitude, Double longitude, Long id) {
        HelpIncident helpIncident = getHelpIncident(id);
        helpIncident.setLatitude(latitude.toString());
        helpIncident.setLongitude(longitude.toString());
        return helpIncidentRepository.save(helpIncident);
    }

    @Override
    public HelpIncident updateStatusDone(Long id) {

        HelpIncident helpIncident = getHelpIncident(id);
        helpIncident.setStatusHelpIncident(StatusHelpIncident.CLOSED);
        helpIncident.setDateEnded(new Date());
        return helpIncidentRepository.save(helpIncident);
    }


    @Override
    public List<HelpIncident> getAllHelpIncidents() {
        return helpIncidentRepository.findAll();
    }

    @Override
    public List<HelpIncident> getAllHelpIncidentsByUser(Long id) {
        return null;
    }

    @Override
    public HelpIncident getHelpIncident(Long id) {
        Optional<HelpIncident> helpIncident = helpIncidentRepository.findById(id);

        if (helpIncident.isEmpty()) {
            throw new CustomException("HelpIncident not found");
        }
        return helpIncident.get();
    }

    @Override
    public HelpIncident assignHelper(Long id, HelpIncidentAssignDto helpIncidentAssignDto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Optional<HelpIncident> helpIncident = helpIncidentRepository.findByChatConnectionIdAndStatusHelpIncident(id, StatusHelpIncident.OPEN);

        if (helpIncident.isEmpty()) {
            throw new CustomException("HelpIncident not found");
        }

        Optional<User> user = userRepository.findById(claims.getUserId());

        if (user.isEmpty()) {
            throw new CustomException("User not found");
        }
        helpIncident.get().setHelper(user.get());
        helpIncident.get().setDateStarted(new Date());
        helpIncident.get().setLongitude(helpIncidentAssignDto.getLongitude());
        helpIncident.get().setLatitude(helpIncidentAssignDto.getLatitude());
        return helpIncidentRepository.save(helpIncident.get());

    }

    @Override
    public String getHelpIncidentByUsername(String username) {

        ChatConnection chatConnection = chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING);
        if (chatConnection == null) {
            return "";
        }

        Optional<HelpIncident> helpIncident = helpIncidentRepository.findByChatConnectionIdAndStatusHelpIncident(chatConnection.getId(), StatusHelpIncident.OPEN);
        if (helpIncident.isEmpty()) {
            return "";
        }
        return helpIncident.get().getIncidentCategory().getName();
    }

    @Override
    public HelpIncident findHelpIncidentByHelperIdAndStatus(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Optional<HelpIncident> helpIncident = helpIncidentRepository.findByHelperIdAndStatusHelpIncident(claims.getUserId(), StatusHelpIncident.OPEN);
        return helpIncident.orElse(null);


    }

    @Override
    public HelpIncident findHelpIncidentByUserIdAndStatus(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Optional<HelpIncident> helpIncident = helpIncidentRepository.findByUserIdAndStatusAndHelperNotNull(claims.getUserId(), StatusHelpIncident.OPEN);
        return helpIncident.orElse(null);
    }


}
