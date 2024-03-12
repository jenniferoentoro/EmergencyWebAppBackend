package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.HelpIncidentAssignDto;
import fontys.emergencywebapps.controllers.dto.HelpIncidentDto;
import fontys.emergencywebapps.persistence.entities.*;
import fontys.emergencywebapps.persistence.repos.ChatConnectionRepository;
import fontys.emergencywebapps.persistence.repos.HelpIncidentRepository;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ImagesServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class HelperIncidentUseCasesImplTest {

    @Mock
    HelpIncidentRepository helpIncidentRepository;

    @Mock
    ChatConnectionRepository chatConnectionRepository;

    @Mock
    IncidentCategoryRepository incidentCategoryRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    HelperIncidentUseCasesImpl helperIncidentUseCases;

    @Test
    void testUpdateHelpIncidentLocation_Success() {
        Long id = 1L;
        Double latitude = 12.34;
        Double longitude = 56.78;

        HelpIncident mockHelpIncident = HelpIncident.builder()
                .id(id)
                .latitude(latitude.toString())
                .longitude(longitude.toString())
                .build();
        when(helpIncidentRepository.findById(id)).thenReturn(Optional.of(mockHelpIncident));
        when(helpIncidentRepository.save(mockHelpIncident)).thenReturn(mockHelpIncident);

        HelpIncident result = helperIncidentUseCases.updateHelpIncidentLocation(latitude, longitude, id);

        assertNotNull(result);
        assertEquals(latitude.toString(), result.getLatitude());
        assertEquals(longitude.toString(), result.getLongitude());
    }


    @Test
    void testUpdateStatusDone_Success() {
        Long id = 1L;
        HelpIncident mockHelpIncident = HelpIncident.builder()
                .id(id)
                .statusHelpIncident(StatusHelpIncident.OPEN)
                .build();
        when(helpIncidentRepository.findById(id)).thenReturn(Optional.of(mockHelpIncident));
        when(helpIncidentRepository.save(mockHelpIncident)).thenReturn(mockHelpIncident);

        HelpIncident result = helperIncidentUseCases.updateStatusDone(id);

        assertNotNull(result);
        assertEquals(StatusHelpIncident.CLOSED, result.getStatusHelpIncident());
    }

    @Test
    void testGetHelpIncident_Success() {
        Long id = 1L;
        HelpIncident mockHelpIncident = HelpIncident.builder()
                .id(id)
                .build();
        when(helpIncidentRepository.findById(id)).thenReturn(Optional.of(mockHelpIncident));

        HelpIncident result = helperIncidentUseCases.getHelpIncident(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

//    @Test
//    void testAssignHelper_Success() {
//        Long id = 1L;
//        Long helperId = 2L;
//        Double latitude = 12.34;
//        Double longitude = 56.78;
//        HelpIncidentAssignDto mockHelpIncidentAssignDto = HelpIncidentAssignDto.builder()
//                .helperId(helperId)
//                .latitude(latitude.toString())
//                .longitude(longitude.toString())
//                .build();
//        HelpIncident mockHelpIncident = HelpIncident.builder()
//                .id(id)
//                .build();
//        when(helpIncidentRepository.findById(id)).thenReturn(Optional.of(mockHelpIncident));
//
//        User mockUser = User.builder()
//                .id(helperId)
//                .build();
//        when(userRepository.findById(helperId)).thenReturn(Optional.of(mockUser));
//        when(helpIncidentRepository.save(mockHelpIncident)).thenReturn(mockHelpIncident);
//
//        HelpIncident result = helperIncidentUseCases.assignHelper(id, mockHelpIncidentAssignDto);
//
//        assertNotNull(result);
//        assertEquals(helperId, result.getHelper().getId());
//        assertEquals(latitude.toString(), result.getLatitude());
//        assertEquals(longitude.toString(), result.getLongitude());
//    }

//    @Test
//    void testCreateHelpIncident_Success() {
//        // Mocking data
//        Long chatConnectionId = 1L;
//        Long incidentCategoryId = 2L;
//        ChatConnection mockChatConnection = ChatConnection.builder()
//                .id(chatConnectionId)
//                .build();
//        IncidentCategory mockIncidentCategory = IncidentCategory.builder()
//                .id(incidentCategoryId)
//                .build();
//        HelpIncident mockHelpIncident = HelpIncident.builder()
//                .id(null)
//                .chatConnection(mockChatConnection)
//                .incidentCategory(mockIncidentCategory)
//                .statusHelpIncident(StatusHelpIncident.OPEN)
//                .build();
//        HelpIncidentDto mockHelpIncidentDto = HelpIncidentDto.builder()
//                .incidentCategoryId(incidentCategoryId)
//                .build();
//        when(chatConnectionRepository.findById(chatConnectionId)).thenReturn(Optional.of(mockChatConnection));
//        when(incidentCategoryRepository.findById(incidentCategoryId)).thenReturn(Optional.of(mockIncidentCategory));
//
//        when(helpIncidentRepository.save(mockHelpIncident))
//                .thenAnswer(invocation -> {
//                    HelpIncident savedHelpIncident = invocation.getArgument(0);
//                    assertEquals(mockHelpIncident.getChatConnection(), savedHelpIncident.getChatConnection());
//                    assertEquals(mockHelpIncident.getIncidentCategory(), savedHelpIncident.getIncidentCategory());
//                    return savedHelpIncident;  // Return the savedHelpIncident
//                });
//
//        HelpIncident result = helperIncidentUseCases.createHelpIncident(mockHelpIncidentDto, "username");
//
//        assertNotNull(result);
//        assertEquals(chatConnectionId, result.getChatConnection().getId());
//        assertEquals(incidentCategoryId, result.getIncidentCategory().getId());
//    }


    @Test
    void testGetAllHelpIncidents_Success() {
        when(helpIncidentRepository.findAll()).thenReturn(List.of(new HelpIncident()));
        Iterable<HelpIncident> result = helperIncidentUseCases.getAllHelpIncidents();
        assertSame(result, helpIncidentRepository.findAll());
    }


    @Test
    void testCreateHelpIncident_Success() {
        String username = "testUser";
        Long chatConnectionId = 1L;
        Long incidentCategoryId = 2L;
        ChatConnection mockChatConnection = ChatConnection.builder()
                .id(chatConnectionId)
                .statusChatConnection(StatusChatConnection.ONGOING)
                .build();
        IncidentCategory mockIncidentCategory = IncidentCategory.builder()
                .id(incidentCategoryId)
                .build();
        HelpIncidentDto mockHelpIncidentDto = HelpIncidentDto.builder()
                .incidentCategoryId(incidentCategoryId)
                .build();
        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING))
                .thenReturn(mockChatConnection);
        when(incidentCategoryRepository.findById(incidentCategoryId)).thenReturn(Optional.of(mockIncidentCategory));

        when(helpIncidentRepository.save(Mockito.any(HelpIncident.class)))
                .thenAnswer(invocation -> {
                    HelpIncident savedHelpIncident = invocation.getArgument(0);
                    assertEquals(mockChatConnection, savedHelpIncident.getChatConnection());
                    assertEquals(mockIncidentCategory, savedHelpIncident.getIncidentCategory());
                    assertEquals(StatusHelpIncident.OPEN, savedHelpIncident.getStatusHelpIncident());
                    return savedHelpIncident;  // Return the savedHelpIncident
                });

        HelpIncident result = helperIncidentUseCases.createHelpIncident(mockHelpIncidentDto, username);

        assertNotNull(result);
        assertEquals(chatConnectionId, result.getChatConnection().getId());
        assertEquals(incidentCategoryId, result.getIncidentCategory().getId());
        assertEquals(StatusHelpIncident.OPEN, result.getStatusHelpIncident());
    }

    @Test
    void testCreateHelpIncident_InvalidChatConnection() {
        String username = "testUser";
        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING))
                .thenReturn(null);

        HelpIncidentDto mockHelpIncidentDto = HelpIncidentDto.builder()
                .incidentCategoryId(2L)
                .build();

        assertThrows(CustomException.class,
                () -> helperIncidentUseCases.createHelpIncident(mockHelpIncidentDto, username),
                "ChatConnection not found");
    }

    @Test
    void testCreateHelpIncident_InvalidIncidentCategory() {
        String username = "testUser";
        Long chatConnectionId = 1L;
        ChatConnection mockChatConnection = ChatConnection.builder()
                .id(chatConnectionId)
                .statusChatConnection(StatusChatConnection.ONGOING)
                .build();
        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING))
                .thenReturn(mockChatConnection);
        when(incidentCategoryRepository.findById(2L)).thenReturn(Optional.empty());

        HelpIncidentDto mockHelpIncidentDto = HelpIncidentDto.builder()
                .incidentCategoryId(2L)
                .build();

        assertThrows(CustomException.class,
                () -> helperIncidentUseCases.createHelpIncident(mockHelpIncidentDto, username),
                "IncidentCategory not found");
    }

    @Test
    void testGetHelpIncidentByUsername_Success() {
        String username = "testUser";
        Long chatConnectionId = 1L;
        Long incidentCategoryId = 2L;
        ChatConnection mockChatConnection = ChatConnection.builder()
                .id(chatConnectionId)
                .statusChatConnection(StatusChatConnection.ONGOING)
                .build();
        IncidentCategory mockIncidentCategory = IncidentCategory.builder()
                .id(incidentCategoryId)
                .name("Test Incident")
                .build();
        HelpIncident mockHelpIncident = HelpIncident.builder()
                .id(3L)
                .chatConnection(mockChatConnection)
                .incidentCategory(mockIncidentCategory)
                .statusHelpIncident(StatusHelpIncident.OPEN)
                .build();

        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING))
                .thenReturn(mockChatConnection);
        when(helpIncidentRepository.findByChatConnectionIdAndStatusHelpIncident(chatConnectionId, StatusHelpIncident.OPEN))
                .thenReturn(Optional.of(mockHelpIncident));

        String result = helperIncidentUseCases.getHelpIncidentByUsername(username);

        assertEquals("Test Incident", result);
    }

    @Test
    void testGetHelpIncidentByUsername_NoChatConnection() {
        String username = "testUser";
        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING))
                .thenReturn(null);

        String result = helperIncidentUseCases.getHelpIncidentByUsername(username);

        assertEquals("", result);
    }

    @Test
    void testGetHelpIncidentByUsername_NoHelpIncident() {
        // Mocking data
        String username = "testUser";
        Long chatConnectionId = 1L;
        ChatConnection mockChatConnection = ChatConnection.builder()
                .id(chatConnectionId)
                .statusChatConnection(StatusChatConnection.ONGOING)
                .build();

        when(chatConnectionRepository.findByUsernameAndStatusChatConnectionCustom(username, StatusChatConnection.ONGOING))
                .thenReturn(mockChatConnection);
        when(helpIncidentRepository.findByChatConnectionIdAndStatusHelpIncident(chatConnectionId, StatusHelpIncident.OPEN))
                .thenReturn(Optional.empty());

        String result = helperIncidentUseCases.getHelpIncidentByUsername(username);

        assertEquals("", result);
    }

}