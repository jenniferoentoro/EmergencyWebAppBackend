package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionRequest;
import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionUpdateRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReportDiscussion;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.IncidentReportDiscussionRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {IncidentReportDiscussionServiceImpl.class})
class IncidentReportDiscussionServiceImplTest {
    @Mock
    private IncidentReportDiscussionRepository incidentReportDiscussionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessTokenDecoder jwtService;

    @InjectMocks
    private IncidentReportDiscussionServiceImpl incidentReportDiscussionService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void testGetIncidentReportDiscussion_ValidId_ShouldReturnIncidentReportDiscussion() {
        Long discussionId = 1L;
        IncidentReportDiscussion mockDiscussion = new IncidentReportDiscussion();
        when(incidentReportDiscussionRepository.findById(discussionId)).thenReturn(Optional.of(mockDiscussion));

        IncidentReportDiscussion result = incidentReportDiscussionService.getIncidentReportDiscussion(discussionId);

        assertNotNull(result);
        assertEquals(mockDiscussion, result);
        verify(incidentReportDiscussionRepository, times(1)).findById(discussionId);
    }

    @Test
    void testGetIncidentReportDiscussion_InvalidId_ShouldThrowCustomException() {
        Long invalidDiscussionId = 2L;
        when(incidentReportDiscussionRepository.findById(invalidDiscussionId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportDiscussionService.getIncidentReportDiscussion(invalidDiscussionId), "Incident report discussion not found");
        verify(incidentReportDiscussionRepository, times(1)).findById(invalidDiscussionId);
    }

    @Test
    void testCreateIncidentReportDiscussion_ValidRequest_ShouldCreateDiscussion() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        IncidentReportDiscussionRequest request = new IncidentReportDiscussionRequest();
        request.setDescription("Test Discussion");
        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(anyString())).thenReturn(claims);

        User mockUser = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        IncidentReportDiscussion mockDiscussion = new IncidentReportDiscussion();
        when(modelMapper.map(request, IncidentReportDiscussion.class)).thenReturn(mockDiscussion);
        when(incidentReportDiscussionRepository.save(mockDiscussion)).thenReturn(mockDiscussion);

        IncidentReportDiscussion result = incidentReportDiscussionService.createIncidentReportDiscussion(request, servletRequest);

        assertNotNull(result);
        assertEquals(mockDiscussion, result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(incidentReportDiscussionRepository, times(1)).save(mockDiscussion);
    }

    @Test
    void testCreateIncidentReportDiscussion_UserNotFound_ShouldThrowCustomException() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        IncidentReportDiscussionRequest request = new IncidentReportDiscussionRequest();
        request.setDescription("Test Discussion");

        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(anyString())).thenReturn(claims);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportDiscussionService.createIncidentReportDiscussion(request, servletRequest), "User not found");
        verify(userRepository, times(1)).findById(anyLong());
        verify(incidentReportDiscussionRepository, never()).save(any());
    }

//    @Test
//    void testUpdateIncidentReportDiscussion_ValidRequest_ShouldUpdateDiscussion() {
//        HttpServletRequest servletRequest = (HttpServletRequest) mock(HttpServletRequest.class, lenient());
//        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");
//
//        Long discussionId = 1L;
//        IncidentReportDiscussionUpdateRequest request = new IncidentReportDiscussionUpdateRequest();
//        request.setDescription("Updated description");
//
//        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
//        when(jwtService.decode(anyString())).thenReturn(claims);
//
//        User mockUser = new User();
//        mockUser.setId(1L);
//        IncidentReportDiscussion mockDiscussion = new IncidentReportDiscussion();
//        mockDiscussion.setId(discussionId);
//        mockDiscussion.setUser(mockUser);
//        mockDiscussion.setDescription("Original description");
//        mockDiscussion.setDate(new Date());
//
//        when(incidentReportDiscussionRepository.findById(discussionId)).thenReturn(Optional.of(mockDiscussion));
//        when(modelMapper.map(request, IncidentReportDiscussion.class)).thenReturn(mockDiscussion);
//        when(incidentReportDiscussionRepository.save(mockDiscussion)).thenReturn(mockDiscussion);
//
//        IncidentReportDiscussion result = incidentReportDiscussionService.updateIncidentReportDiscussion(discussionId, request, servletRequest);
//
//        assertNotNull(result);
//        assertEquals("Updated description", result.getDescription());
//        verify(incidentReportDiscussionRepository, times(1)).findById(discussionId);
//        verify(incidentReportDiscussionRepository, times(1)).save(mockDiscussion);
//    }

    @Test
    void testUpdateIncidentReportDiscussion_DiscussionNotFound_ShouldThrowCustomException() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        Long discussionId = 1L;
        IncidentReportDiscussionUpdateRequest request = new IncidentReportDiscussionUpdateRequest();
        request.setDescription("Updated description");

        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(anyString())).thenReturn(claims);

        when(incidentReportDiscussionRepository.findById(discussionId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportDiscussionService.updateIncidentReportDiscussion(discussionId, request, servletRequest), "Incident report discussion not found");
        verify(incidentReportDiscussionRepository, never()).save(any());
    }

    @Test
    void testUpdateIncidentReportDiscussion_UnauthorizedUser_ShouldThrowCustomException() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader(anyString())).thenReturn("Bearer mockToken");

        Long discussionId = 1L;
        IncidentReportDiscussionUpdateRequest request = new IncidentReportDiscussionUpdateRequest();
        request.setDescription("Updated description");

        AccessTokenImpl claims = new AccessTokenImpl("mockToken", 2L, Collections.singleton("USER"));
        when(jwtService.decode(anyString())).thenReturn(claims);

        User mockUser = new User();
        mockUser.setId(1L);
        IncidentReportDiscussion mockDiscussion = new IncidentReportDiscussion();
        mockDiscussion.setId(discussionId);
        mockDiscussion.setUser(mockUser);
        mockDiscussion.setDescription("Original description");
        mockDiscussion.setDate(new Date());

        when(incidentReportDiscussionRepository.findById(discussionId)).thenReturn(Optional.of(mockDiscussion));

        assertThrows(CustomException.class, () -> incidentReportDiscussionService.updateIncidentReportDiscussion(discussionId, request, servletRequest), "You are not allowed to update this incident report discussion");
        verify(incidentReportDiscussionRepository, never()).save(any());
    }

    @Test
    void testDeleteIncidentReportDiscussion_ExistingId_ShouldDeleteDiscussion() {
        Long discussionId = 1L;
        IncidentReportDiscussion mockDiscussion = new IncidentReportDiscussion();
        mockDiscussion.setId(discussionId);

        when(incidentReportDiscussionRepository.findById(discussionId)).thenReturn(Optional.of(mockDiscussion));

        assertDoesNotThrow(() -> incidentReportDiscussionService.deleteIncidentReportDiscussion(discussionId));

        verify(incidentReportDiscussionRepository, times(1)).findById(discussionId);
        verify(incidentReportDiscussionRepository, times(1)).deleteById(discussionId);
    }

    @Test
    void testDeleteIncidentReportDiscussion_NonExistingId_ShouldThrowCustomException() {
        Long discussionId = 1L;

        when(incidentReportDiscussionRepository.findById(discussionId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportDiscussionService.deleteIncidentReportDiscussion(discussionId));

        verify(incidentReportDiscussionRepository, times(1)).findById(discussionId);
        verify(incidentReportDiscussionRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void testGetAllIncidentReportDiscussion_ShouldReturnAllDiscussions() {
        List<IncidentReportDiscussion> mockDiscussions = Arrays.asList(
                new IncidentReportDiscussion(), new IncidentReportDiscussion(), new IncidentReportDiscussion()
        );

        when(incidentReportDiscussionRepository.findAll()).thenReturn(mockDiscussions);

        Iterable<IncidentReportDiscussion> result = incidentReportDiscussionService.getAllIncidentReportDiscussion();

        assertNotNull(result);
        assertEquals(mockDiscussions, result);
        verify(incidentReportDiscussionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllIncidentReportDiscussionByIncidentReportId_ShouldReturnDiscussionsByIncidentReportId() {
        Long incidentReportId = 1L;
        List<IncidentReportDiscussion> mockDiscussions = Arrays.asList(
                new IncidentReportDiscussion(), new IncidentReportDiscussion(), new IncidentReportDiscussion()
        );

        when(incidentReportDiscussionRepository.findAllByIncidentReportId(incidentReportId)).thenReturn(mockDiscussions);

        Iterable<IncidentReportDiscussion> result = incidentReportDiscussionService.getAllIncidentReportDiscussionByIncidentReportId(incidentReportId);

        assertNotNull(result);
        assertEquals(mockDiscussions, result);
        verify(incidentReportDiscussionRepository, times(1)).findAllByIncidentReportId(incidentReportId);
    }

}