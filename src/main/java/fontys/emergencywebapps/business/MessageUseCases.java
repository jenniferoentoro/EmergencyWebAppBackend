package fontys.emergencywebapps.business;

import fontys.emergencywebapps.persistence.entities.Message;
import jakarta.servlet.http.HttpServletRequest;
import fontys.emergencywebapps.controllers.dto.MessageAdminRequest;
import fontys.emergencywebapps.controllers.dto.MessageRequest;

import java.util.List;

public interface MessageUseCases {
    void sendMessage(MessageRequest message, HttpServletRequest request);

    void openAIMessage(MessageRequest message, HttpServletRequest request);

    void adminMessage(MessageAdminRequest message);
    List<Message> getMessages(String username);
}
