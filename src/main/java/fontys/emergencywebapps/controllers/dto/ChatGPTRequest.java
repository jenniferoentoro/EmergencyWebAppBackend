package fontys.emergencywebapps.controllers.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ChatGPTRequest {
    private String model;
    private List<MessageGPT> messages;

    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new MessageGPT("user", prompt));
    }
}
