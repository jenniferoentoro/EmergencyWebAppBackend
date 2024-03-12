package fontys.emergencywebapps.business;

import fontys.emergencywebapps.persistence.entities.ChatConnection;
import jakarta.servlet.http.HttpServletRequest;
import fontys.emergencywebapps.controllers.dto.ChatConnectionRequest;
import fontys.emergencywebapps.controllers.dto.ChatConnectionUpdateDto;

import java.util.List;

public interface ChatConnectionUseCases {
    void connect(ChatConnectionRequest request, HttpServletRequest servletRequest);

    void reconnect(ChatConnectionRequest request, HttpServletRequest servletRequest);

    void endChatConnection(HttpServletRequest servletRequest);

    void endChatConnectionByUsername(String username);

    void updateTypeConnectionAdmin(ChatConnectionUpdateDto updateDto);

    void updateTypeConnectionOPENAI(ChatConnectionUpdateDto updateDto);

    ChatConnection findByUserIdAndStatusOngoing(HttpServletRequest servletRequest);

    int countByStatusOngoing();

    int countByStatusEnded();

    int countByStatusOngoingAndTypeConnectionOPENAI();

    int countByStatusOngoingAndTypeConnectionAdmin();


    List<ChatConnection> findAllByStatusOngoing(HttpServletRequest servletRequest);

}
