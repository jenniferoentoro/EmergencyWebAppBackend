package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.business.MessageUseCases;
import fontys.emergencywebapps.controllers.dto.MessageAdminRequest;
import fontys.emergencywebapps.controllers.dto.MessageDto;
import fontys.emergencywebapps.controllers.dto.MessageRequest;
import fontys.emergencywebapps.persistence.entities.Message;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessageController {

    private final MessageUseCases messageUseCases;
    private final ModelMapper modelMapper;

    @PostMapping
    public void sendMessage(@RequestBody MessageRequest message, HttpServletRequest request) {
        messageUseCases.sendMessage(message, request);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable("username") String username) {
        List<Message> messages = messageUseCases.getMessages(username);

        List<MessageDto> messageRequests = new ArrayList<>();
        for (Message message : messages) {
            MessageDto messageRequest = modelMapper.map(message, MessageDto.class);
            messageRequests.add(messageRequest);

        }
        return ResponseEntity.ok(messageRequests);
    }

    @PostMapping("/admin")
    public void adminMessage(@RequestBody MessageAdminRequest message) {

        messageUseCases.adminMessage(message);
    }
}