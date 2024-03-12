package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.MessageUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.persistence.entities.*;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.controllers.dto.MessageAdminRequest;
import fontys.emergencywebapps.controllers.dto.MessageRequest;
import fontys.emergencywebapps.persistence.repos.ChatConnectionRepository;
import fontys.emergencywebapps.persistence.repos.MessageRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageUseCases {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final ChatConnectionRepository chatConnectionRepository;
    private static final String NOT_FOUND = "User not found";
    private final AccessTokenDecoder jwtService;

    @Override
    public void sendMessage(MessageRequest message, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Message messageSend = modelMapper.map(message, Message.class);
        Optional<User> user = userRepository.findById(claims.getUserId());

        messageSend.setMessage(message.getMessage());
        if (user.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        messageSend.setSender(user.get());
        messageSend.setDate(new Date());
        ChatConnection chatConnection = chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING);
        messageSend.setStatusMsg(StatusMsg.SENT);
        messageSend.setChatConnection(chatConnection);
        messageRepository.save(messageSend);
    }

    @Override
    public void openAIMessage(MessageRequest message, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

        Message messageSend = modelMapper.map(message, Message.class);
        Optional<User> user = userRepository.findById(claims.getUserId());
        if(user.isEmpty()){
            throw new CustomException(NOT_FOUND);
        }
        messageSend.setSender(user.get());
        messageSend.setDate(new Date());
        ChatConnection chatConnection = chatConnectionRepository.findByUserIdAndStatusChatConnection(claims.getUserId(), StatusChatConnection.ONGOING);
        messageSend.setChatConnection(chatConnection);
        messageSend.setStatusMsg(StatusMsg.OPENAI);
        messageRepository.save(messageSend);
    }

    @Override
    public void adminMessage(MessageAdminRequest message) {

        Message messageSend = modelMapper.map(message, Message.class);
        Optional<User> user = userRepository.findByUsername(message.getUsernameUser());

        if(user.isEmpty()){
            throw new CustomException(NOT_FOUND);
        }

        messageSend.setSender(user.get());
        messageSend.setDate(new Date());
        ChatConnection chatConnection = chatConnectionRepository.findByUserIdAndStatusChatConnection(user.get().getId(), StatusChatConnection.ONGOING);
        messageSend.setChatConnection(chatConnection);
        messageSend.setStatusMsg(StatusMsg.ADMIN);
        messageRepository.save(messageSend);
    }

    @Override
    public List<Message> getMessages(String username) {

        return messageRepository.findMessagesByUserIdAndOngoingChatConnection(username);
    }


}
