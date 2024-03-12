package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.controllers.dto.ChatConnectionRequest;
import fontys.emergencywebapps.controllers.dto.ChatConnectionUpdateDto;
import fontys.emergencywebapps.persistence.entities.ChatConnection;
import fontys.emergencywebapps.persistence.entities.StatusChatConnection;
import fontys.emergencywebapps.persistence.entities.TypeConnection;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.ChatConnectionRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ChatConnectionServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class ChatConnectionServiceImplTest {
    @Mock
    private ChatConnectionRepository chatConnectionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessTokenDecoder jwtService;

    @InjectMocks
    private ChatConnectionServiceImpl chatConnectionService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void testConnect_ValidRequest_ShouldConnect() {
        ChatConnectionRequest request = new ChatConnectionRequest();
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);

        User mockUser = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(anyLong(), any())).thenReturn(null);

        when(modelMapper.map(any(), eq(ChatConnection.class))).thenReturn(new ChatConnection());

        assertDoesNotThrow(() -> chatConnectionService.connect(request, servletRequest));

        verify(chatConnectionRepository, times(1)).save(any(ChatConnection.class));
    }


    @Test
    void testConnect_UserNotFound_ShouldThrowCustomException() {
        ChatConnectionRequest request = new ChatConnectionRequest();
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        when(modelMapper.map(any(), eq(ChatConnection.class))).thenReturn(new ChatConnection());

        assertThrows(CustomException.class, () -> chatConnectionService.connect(request, servletRequest), "User not found");
    }

    @Test
    void testReconnect_ValidRequest_ShouldReconnect() {
        ChatConnectionRequest request = new ChatConnectionRequest();
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = mock(AccessTokenImpl.class);
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);

        User mockUser = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(anyLong(), any())).thenReturn(null);

        when(modelMapper.map(any(), eq(ChatConnection.class))).thenReturn(new ChatConnection());

        assertDoesNotThrow(() -> chatConnectionService.reconnect(request, servletRequest));

        verify(chatConnectionRepository, times(1)).save(any(ChatConnection.class));
    }

    @Test
    void testReconnect_UserNotFound_ShouldThrowCustomException() {
        ChatConnectionRequest request = new ChatConnectionRequest();
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = mock(AccessTokenImpl.class);
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        when(modelMapper.map(any(), eq(ChatConnection.class))).thenReturn(new ChatConnection());

        assertThrows(CustomException.class, () -> chatConnectionService.reconnect(request, servletRequest), "User not found");
    }

    @Test
    void testUpdateTypeConnectionAdmin_ValidUpdate_ShouldUpdateTypeConnection() {
        ChatConnectionUpdateDto updateDto = new ChatConnectionUpdateDto();
        updateDto.setUsername("testUser");

        ChatConnection mockChatConnection = new ChatConnection();
        mockChatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);
        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(anyString(), any())).thenReturn(mockChatConnection);

        chatConnectionService.updateTypeConnectionAdmin(updateDto);

        assertEquals(TypeConnection.ADMIN, mockChatConnection.getTypeConnection());
        verify(chatConnectionRepository, times(1)).save(mockChatConnection);
    }

    @Test
    void testUpdateTypeConnectionOPENAI_ValidUpdate_ShouldUpdateTypeConnection() {
        ChatConnectionUpdateDto updateDto = new ChatConnectionUpdateDto();
        updateDto.setUsername("testUser");

        ChatConnection mockChatConnection = new ChatConnection();
        mockChatConnection.setStatusChatConnection(StatusChatConnection.ONGOING);
        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(anyString(), any())).thenReturn(mockChatConnection);

        chatConnectionService.updateTypeConnectionOPENAI(updateDto);

        assertEquals(TypeConnection.OPENAI, mockChatConnection.getTypeConnection());
        verify(chatConnectionRepository, times(1)).save(mockChatConnection);
    }

    @Test
    void testFindByUserIdAndStatusOngoing_ValidRequest_ShouldReturnChatConnection() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = mock(AccessTokenImpl.class);
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);
        when(mockAccessToken.getUserId()).thenReturn(1L);

        ChatConnection mockChatConnection = new ChatConnection();
        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(anyLong(), any())).thenReturn(mockChatConnection);

        ChatConnection result = chatConnectionService.findByUserIdAndStatusOngoing(servletRequest);

        assertNotNull(result);
        assertEquals(mockChatConnection, result);
    }

    @Test
    void testFindByUserIdAndStatusOngoing_UserNotFound_ShouldReturnNull() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);

        ChatConnection result = chatConnectionService.findByUserIdAndStatusOngoing(servletRequest);

        assertNull(result);
    }


    @Test
    void testFindByUserIdAndStatusOngoing_NoChatConnectionFound_ShouldReturnNull() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = mock(AccessTokenImpl.class);
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);
        when(mockAccessToken.getUserId()).thenReturn(1L);

        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(anyLong(), any())).thenReturn(null);

        ChatConnection result = chatConnectionService.findByUserIdAndStatusOngoing(servletRequest);

        assertNull(result);
    }

    @Test
    void testEndChatConnection_Success() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = mock(AccessTokenImpl.class);
        when(jwtService.decode(anyString())).thenReturn(mockAccessToken);
        when(mockAccessToken.getUserId()).thenReturn(1L);

        ChatConnection mockChatConnection = new ChatConnection();
        when(chatConnectionRepository.findByUserIdAndStatusChatConnection(anyLong(), any())).thenReturn(mockChatConnection);

        chatConnectionService.endChatConnection(servletRequest);

        assertEquals(StatusChatConnection.ENDED, mockChatConnection.getStatusChatConnection());
        verify(chatConnectionRepository, times(1)).save(mockChatConnection);
    }

    @Test
    void testEndChatConnectionByUsername_Success() {
        String username = "testUser";

        ChatConnection mockChatConnection = new ChatConnection();
        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING))
                .thenReturn(mockChatConnection);

        chatConnectionService.endChatConnectionByUsername(username);

        assertEquals(StatusChatConnection.ENDED, mockChatConnection.getStatusChatConnection());
        verify(chatConnectionRepository, times(1)).save(mockChatConnection);
    }

}