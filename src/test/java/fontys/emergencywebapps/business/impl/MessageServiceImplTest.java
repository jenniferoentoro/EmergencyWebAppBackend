package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.controllers.dto.MessageAdminRequest;
import fontys.emergencywebapps.controllers.dto.MessageRequest;
import fontys.emergencywebapps.persistence.entities.*;
import fontys.emergencywebapps.persistence.repos.ChatConnectionRepository;
import fontys.emergencywebapps.persistence.repos.MessageRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {MessageServiceImpl.class})
class MessageServiceImplTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ChatConnectionRepository chatConnectionRepository;

    @Mock
    private AccessTokenDecoder jwtService;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void sendMessage_ValidMessage_ShouldSaveMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "Bearer mockToken";
        when(request.getHeader("Authorization")).thenReturn(token);

        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(token.substring(7))).thenReturn(claims);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("Hello, World!");

        User sender = new User();
        sender.setId(1L);
        when(userRepository.findById(claims.getUserId())).thenReturn(Optional.of(sender));

        ChatConnection chatConnection = new ChatConnection();
        chatConnection.setUser(sender);
        chatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);
        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING))
                .thenReturn(chatConnection);

        Message mappedMessage = new Message();
        when(modelMapper.map(messageRequest, Message.class)).thenReturn(mappedMessage);

        assertDoesNotThrow(() -> messageService.sendMessage(messageRequest, request));

        verify(messageRepository, times(1)).save(mappedMessage);
        assertEquals(sender, mappedMessage.getSender());
        assertEquals(StatusMsg.SENT, mappedMessage.getStatusMsg());
        assertNotNull(mappedMessage.getDate());
        assertEquals(chatConnection, mappedMessage.getChatConnection());
    }

//    @Test
//    void sendMessage_UserNotFound_ShouldThrowCustomException() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        String token = "Bearer mockToken";
//        when(request.getHeader("Authorization")).thenReturn(token);
//
//        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
//        when(jwtService.decode(token.substring(7))).thenReturn(claims);
//
//        MessageRequest messageRequest = new MessageRequest();
//        messageRequest.setMessage("Hello, World!");
//
//        when(userRepository.findById(claims.getUserId())).thenReturn(Optional.empty());
//
//        assertThrows(CustomException.class,
//                () -> messageService.sendMessage(messageRequest, request));
//
//        verify(messageRepository, never()).save(any(Message.class));
//    }

    @Test
    void openAIMessage_ValidMessage_ShouldSaveMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "Bearer mockToken";
        when(request.getHeader("Authorization")).thenReturn(token);

        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(token.substring(7))).thenReturn(claims);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("Hello, OpenAI!");

        User sender = new User();
        sender.setId(1L);
        when(userRepository.findById(claims.getUserId())).thenReturn(Optional.of(sender));

        ChatConnection chatConnection = new ChatConnection();
        chatConnection.setUser(sender);
        chatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);
        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING))
                .thenReturn(chatConnection);

        Message mappedMessage = new Message();
        when(modelMapper.map(messageRequest, Message.class)).thenReturn(mappedMessage);

        assertDoesNotThrow(() -> messageService.openAIMessage(messageRequest, request));

        verify(messageRepository, times(1)).save(mappedMessage);
        assertEquals(sender, mappedMessage.getSender());
        assertEquals(StatusMsg.OPENAI, mappedMessage.getStatusMsg());
        assertNotNull(mappedMessage.getDate());
        assertEquals(chatConnection, mappedMessage.getChatConnection());
    }

    @Test
    void openAIMessage_UserNotFound_ShouldThrowCustomException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "Bearer mockToken";
        when(request.getHeader("Authorization")).thenReturn(token);

        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(token.substring(7))).thenReturn(claims);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage("Hello, OpenAI!");

        when(userRepository.findById(claims.getUserId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class,
                () -> messageService.openAIMessage(messageRequest, request));

        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void adminMessage_ValidMessage_ShouldSaveMessage() {
        MessageAdminRequest adminRequest = new MessageAdminRequest();
        adminRequest.setUsernameUser("john.doe");
        adminRequest.setMessage("Important message from admin");

        User user = new User();
        user.setId(1L);
        when(userRepository.findByUsername(adminRequest.getUsernameUser())).thenReturn(Optional.of(user));

        ChatConnection chatConnection = new ChatConnection();
        chatConnection.setUser(user);
        chatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);
        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(user.getId(), StatusChatConnection.ONGOING))
                .thenReturn(chatConnection);

        Message mappedMessage = new Message();
        when(modelMapper.map(adminRequest, Message.class)).thenReturn(mappedMessage);

        assertDoesNotThrow(() -> messageService.adminMessage(adminRequest));

        verify(messageRepository, times(1)).save(mappedMessage);
        assertEquals(user, mappedMessage.getSender());
        assertEquals(StatusMsg.ADMIN, mappedMessage.getStatusMsg());
        assertNotNull(mappedMessage.getDate());
        assertEquals(chatConnection, mappedMessage.getChatConnection());
    }

    @Test
    void adminMessage_UserNotFound_ShouldThrowCustomException() {
        MessageAdminRequest adminRequest = new MessageAdminRequest();
        adminRequest.setUsernameUser("john.doe");
        adminRequest.setMessage("Important message from admin");

        when(userRepository.findByUsername(adminRequest.getUsernameUser())).thenReturn(Optional.empty());

        assertThrows(CustomException.class,
                () -> messageService.adminMessage(adminRequest));

        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void getMessages_ValidUsername_ShouldReturnMessages() {
        String username = "john.doe";

        List<Message> expectedMessages = new ArrayList<>();
        when(messageRepository.findMessagesByUserIdAndOngoingChatConnection(username)).thenReturn(expectedMessages);

        List<Message> result = messageService.getMessages(username);

        assertEquals(expectedMessages, result);
    }

}