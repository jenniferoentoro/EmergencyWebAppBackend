package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionUpdateRequest;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.IncidentReportDiscussionRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.IncidentDiscussionUseCases;
import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReportDiscussion;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class IncidentReportDiscussionServiceImpl implements IncidentDiscussionUseCases {

    private final IncidentReportDiscussionRepository incidentReportDiscussionRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AccessTokenDecoder jwtService;
    private static final String NOT_FOUND = "Incident report discussion not found";

    @Override
    public IncidentReportDiscussion getIncidentReportDiscussion(Long id) {
        Optional<IncidentReportDiscussion> incidentReportDiscussionResponse = incidentReportDiscussionRepository.findById(id);
        if (incidentReportDiscussionResponse.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        return incidentReportDiscussionResponse.get();
    }

    @Override
    public IncidentReportDiscussion createIncidentReportDiscussion(IncidentReportDiscussionRequest incidentReportDiscussionRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);
        Optional<User> user = userRepository.findById(claims.getUserId());
        if (user.isEmpty()) {
            throw new CustomException("User not found");
        }
        IncidentReportDiscussion incidentReportDiscussion = modelMapper.map(incidentReportDiscussionRequest, IncidentReportDiscussion.class);

        incidentReportDiscussion.setUser(user.get());


        return incidentReportDiscussionRepository.save(incidentReportDiscussion);
    }

    @Override
    public IncidentReportDiscussion updateIncidentReportDiscussion(Long id, IncidentReportDiscussionUpdateRequest incidentReportDiscussionRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Optional<IncidentReportDiscussion> incidentReportDiscussion = incidentReportDiscussionRepository.findById(id);
        if (incidentReportDiscussion.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }

        if (!Objects.equals(claims.getUserId(), incidentReportDiscussion.get().getUser().getId())) {
            throw new CustomException("You are not allowed to update this incident report discussion");
        }
        IncidentReportDiscussion incidentReportDiscussion1 = incidentReportDiscussion.get();

        incidentReportDiscussion1.setDescription(incidentReportDiscussionRequest.getDescription());
        incidentReportDiscussion1.setDate(new Date());

        return incidentReportDiscussionRepository.save(incidentReportDiscussion1);
    }

    @Override
    public void deleteIncidentReportDiscussion(Long id) {
        Optional<IncidentReportDiscussion> incidentReportDiscussion = incidentReportDiscussionRepository.findById(id);
        if (incidentReportDiscussion.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        incidentReportDiscussionRepository.deleteById(id);
    }

    @Override
    public Iterable<IncidentReportDiscussion> getAllIncidentReportDiscussion() {

        return incidentReportDiscussionRepository.findAll();

    }

    @Override
    public Iterable<IncidentReportDiscussion> getAllIncidentReportDiscussionByIncidentReportId(Long id) {
        return  incidentReportDiscussionRepository.findAllByIncidentReportId(id);
    }
}
