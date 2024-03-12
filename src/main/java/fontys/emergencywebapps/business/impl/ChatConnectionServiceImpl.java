package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.persistence.entities.*;
import fontys.emergencywebapps.persistence.repos.HelperInformationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.ChatConnectionUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.ChatConnectionRequest;
import fontys.emergencywebapps.controllers.dto.ChatConnectionUpdateDto;
import fontys.emergencywebapps.persistence.repos.ChatConnectionRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatConnectionServiceImpl implements ChatConnectionUseCases {
    private final ChatConnectionRepository chatConnectionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AccessTokenDecoder jwtService;
    private final HelperInformationRepository helperInformationRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void connect(ChatConnectionRequest request, HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader(AUTHORIZATION_HEADER).substring(7);

        AccessToken claims = jwtService.decode(token);

        ChatConnection chatConnection1 = chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING);
        if (chatConnection1 != null) {
            throw new CustomException("User already connected");
        }

        ChatConnection chatConnection = modelMapper.map(request, ChatConnection.class);
        chatConnection.setTypeConnection(TypeConnection.OPENAI);
        Optional<User> user = userRepository.findById(claims.getUserId());
        if (user.isEmpty()) {
            throw new CustomException("User not found");
        }
        chatConnection.setUser(user.get());

        chatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);

        chatConnectionRepository.save(chatConnection);


    }

    @Override
    public void reconnect(ChatConnectionRequest request, HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader(AUTHORIZATION_HEADER).substring(7);

        AccessToken claims = jwtService.decode(token);


        ChatConnection chatConnection1 = chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING);
        if (chatConnection1 != null) {
            chatConnection1.setStatusChatConnection(StatusChatConnection.ENDED);
            chatConnectionRepository.save(chatConnection1);
        }


        ChatConnection chatConnection = modelMapper.map(request, ChatConnection.class);
        chatConnection.setTypeConnection(TypeConnection.OPENAI);

        Optional<User> user = userRepository.findById(claims.getUserId());
        if (user.isEmpty()) {
            throw new CustomException("User not found");
        }

        chatConnection.setUser(user.get());

        chatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);

        chatConnectionRepository.save(chatConnection);
    }

    @Override
    public void endChatConnection(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader(AUTHORIZATION_HEADER).substring(7);

        AccessToken claims = jwtService.decode(token);

        ChatConnection chatConnection = chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING);
        chatConnection.setStatusChatConnection(StatusChatConnection.ENDED);
        chatConnectionRepository.save(chatConnection);
    }

    @Override
    public void endChatConnectionByUsername(String username) {
        ChatConnection chatConnection = chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING);
        chatConnection.setStatusChatConnection(StatusChatConnection.ENDED);
        chatConnectionRepository.save(chatConnection);
    }

    @Override
    public void updateTypeConnectionAdmin(ChatConnectionUpdateDto updateDto) {
        ChatConnection chatConnection = chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(updateDto.getUsername(), StatusChatConnection.ONGOING);
        chatConnection.setTypeConnection(TypeConnection.ADMIN);
        chatConnectionRepository.save(chatConnection);
    }

    @Override
    public void updateTypeConnectionOPENAI(ChatConnectionUpdateDto updateDto) {
        ChatConnection chatConnection = chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(updateDto.getUsername(), StatusChatConnection.ONGOING);
        chatConnection.setTypeConnection(TypeConnection.OPENAI);
        chatConnectionRepository.save(chatConnection);
    }

    @Override
    public ChatConnection findByUserIdAndStatusOngoing(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader(AUTHORIZATION_HEADER).substring(7);
        AccessToken claims = jwtService.decode(token);
        return chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING);
    }

    @Override
    public int countByStatusOngoing() {
        return chatConnectionRepository.countByStatusChatConnection(StatusChatConnection.ONGOING);
    }

    @Override
    public int countByStatusEnded() {
        return chatConnectionRepository.countByStatusChatConnection(StatusChatConnection.ENDED);
    }

    @Override
    public int countByStatusOngoingAndTypeConnectionOPENAI() {
        return chatConnectionRepository.countByTypeConnectionAndStatusChatConnection(TypeConnection.OPENAI, StatusChatConnection.ONGOING);
    }

    @Override
    public int countByStatusOngoingAndTypeConnectionAdmin() {
        return chatConnectionRepository.countByTypeConnectionAndStatusChatConnection(TypeConnection.ADMIN, StatusChatConnection.ONGOING);
    }

    @Override
    public List<ChatConnection> findAllByStatusOngoing(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader(AUTHORIZATION_HEADER).substring(7);
        AccessToken claims = jwtService.decode(token);

        Optional<HelperInformation> helperInformation = helperInformationRepository.findByUserId(claims.getUserId());
        if (helperInformation.isEmpty()) {
            throw new CustomException("Helper information not found");
        }

        return chatConnectionRepository.findAllByIncidentCategoryAndStatusOpen(helperInformation.get().getIncidentCategory().getId());



    }

}