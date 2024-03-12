package fontys.emergencywebapps.controllers;

import fontys.emergencywebapps.business.ChatConnectionUseCases;
import fontys.emergencywebapps.controllers.dto.ChatConnectionOngoing;
import fontys.emergencywebapps.controllers.dto.ChatConnectionRequest;
import fontys.emergencywebapps.persistence.entities.ChatConnection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fontys.emergencywebapps.controllers.dto.ChatConnectionUpdateDto;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/connect-chat")
public class ChatConnectionContrtoller {
    private final ChatConnectionUseCases chatConnectionService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<String> connect(@Valid @RequestBody ChatConnectionRequest request, HttpServletRequest servletRequest) {

        chatConnectionService.connect(request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PostMapping("/reconnect")
    public ResponseEntity<String> reconnect(@Valid @RequestBody ChatConnectionRequest request, HttpServletRequest servletRequest) {

        chatConnectionService.reconnect(request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }


    @PutMapping("/update-type-connection-admin")
    public ResponseEntity<String> updateTypeConnectionAdmin(@Valid @RequestBody ChatConnectionUpdateDto request) {

        chatConnectionService.updateTypeConnectionAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PutMapping("/update-type-connection-openai")
    public ResponseEntity<String> updateTypeConnectionOPENAI(@Valid @RequestBody ChatConnectionUpdateDto request) {

        chatConnectionService.updateTypeConnectionOPENAI(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @GetMapping("/get-ongoing")
    public ResponseEntity<ChatConnectionOngoing> getOngoing(HttpServletRequest servletRequest) {
        ChatConnection chatConnection = chatConnectionService.findByUserIdAndStatusOngoing(servletRequest);
        return ResponseEntity.ok(modelMapper.map(chatConnection, ChatConnectionOngoing.class));
    }

    @GetMapping("/count-ongoing")
    public ResponseEntity<Integer> countOngoing() {
        return ResponseEntity.ok(chatConnectionService.countByStatusOngoing());
    }

    @GetMapping("/count-ended")
    public ResponseEntity<Integer> countEnded() {
        return ResponseEntity.ok(chatConnectionService.countByStatusEnded());
    }

    @GetMapping("/count-ongoing-openai")
    public ResponseEntity<Integer> countOngoingOPENAI() {
        return ResponseEntity.ok(chatConnectionService.countByStatusOngoingAndTypeConnectionOPENAI());
    }

    @GetMapping("/count-ongoing-admin")
    public ResponseEntity<Integer> countOngoingAdmin() {
        return ResponseEntity.ok(chatConnectionService.countByStatusOngoingAndTypeConnectionAdmin());
    }

    @PutMapping("/end-chat-connection")
    public ResponseEntity<String> endChatConnection(HttpServletRequest servletRequest) {
        chatConnectionService.endChatConnection(servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PutMapping("/end-chat-connection-by-username/{username}")
    public ResponseEntity<String> endChatConnectionByUsername(@PathVariable(value = "username") String username) {
        chatConnectionService.endChatConnectionByUsername(username);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @GetMapping("/get-all-ongoing")
    public ResponseEntity<List<ChatConnection>> getAllOngoing(HttpServletRequest servletRequest) {
        return ResponseEntity.ok(chatConnectionService.findAllByStatusOngoing(servletRequest));
    }

}
