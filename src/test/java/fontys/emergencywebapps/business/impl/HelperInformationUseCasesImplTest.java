package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.controllers.dto.HelperInformationDto;
import fontys.emergencywebapps.persistence.entities.HelpIncident;
import fontys.emergencywebapps.persistence.entities.HelperInformation;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.HelperInformationRepository;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ImagesServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class HelperInformationUseCasesImplTest {
    @Mock
    HelperInformationRepository helperInformationRepository;

    @Mock
    AccessTokenDecoder jwtService;

    @Mock
    UserRepository userRepository;

    @Mock
    IncidentCategoryRepository incidentCategoryRepository;

    @InjectMocks
    HelperInformationUseCasesImpl helperInformationUseCases;


    @Test
    void testCreateHelperInformation_Success() {
        HelperInformationDto helperInformationDto = HelperInformationDto.builder()
                .companyName("Test Company")
                .userId(1L)
                .incidentCategoryId(2L)
                .build();

        User mockUser = User.builder().email("aaaa@gmail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        IncidentCategory mockIncidentCategory = IncidentCategory.builder().name("Test Category").build();
        when(incidentCategoryRepository.findById(2L)).thenReturn(Optional.of(mockIncidentCategory));

        HelperInformation mockHelperInformation = HelperInformation.builder()
                .companyName("Test Company")
                .user(mockUser)
                .incidentCategory(mockIncidentCategory)
                .build();
        when(helperInformationRepository.save(mockHelperInformation)).thenReturn(mockHelperInformation);

        HelperInformation result = helperInformationUseCases.createHelperInformation(helperInformationDto);

        assertNotNull(result);
        assertEquals("Test Company", result.getCompanyName());
        assertEquals(mockUser, result.getUser());
        assertEquals(mockIncidentCategory, result.getIncidentCategory());
    }

    @Test
    void testCreateHelperInformation_UserNotFound() {
        HelperInformationDto helperInformationDto = new HelperInformationDto();
        helperInformationDto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> helperInformationUseCases.createHelperInformation(helperInformationDto));
    }

    @Test
    void testUpdateHelperInformation_Success() {
        HelperInformationDto helperInformationDto = new HelperInformationDto();
        helperInformationDto.setIncidentCategoryId(1L);
        helperInformationDto.setCompanyName("Updated Company");

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");

        when(jwtService.decode("mockToken")).thenReturn(new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER")));

        HelperInformation mockHelperInformation = new HelperInformation();
        mockHelperInformation.setId(1L);
        mockHelperInformation.setCompanyName("Original Company");

        when(helperInformationRepository.findByUserId(1L)).thenReturn(Optional.of(mockHelperInformation));

        IncidentCategory mockIncidentCategory = new IncidentCategory();
        mockIncidentCategory.setId(1L);
        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.of(mockIncidentCategory));

        when(helperInformationRepository.save(mockHelperInformation)).thenReturn(mockHelperInformation);

        HelperInformation result = helperInformationUseCases.updateHelperInformation(helperInformationDto, request);

        assertNotNull(result);
        assertEquals("Updated Company", result.getCompanyName());
        assertEquals(mockIncidentCategory, result.getIncidentCategory());
    }

    @Test
    void testUpdateHelperInformation_HelperInformationNotFound() {
        HelperInformationDto helperInformationDto = new HelperInformationDto();
        helperInformationDto.setIncidentCategoryId(1L);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");

        when(jwtService.decode("mockToken")).thenReturn(new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER")));

        when(helperInformationRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> helperInformationUseCases.updateHelperInformation(helperInformationDto, request));
    }

    @Test
    void testGetHelperInformation_Success() {
        Long helperInformationId = 1L;

        HelperInformation mockHelperInformation = new HelperInformation();
        mockHelperInformation.setId(helperInformationId);
        when(helperInformationRepository.findById(helperInformationId)).thenReturn(Optional.of(mockHelperInformation));

        HelperInformation result = helperInformationUseCases.getHelperInformation(helperInformationId);

        assertNotNull(result);
        assertEquals(helperInformationId, result.getId());
    }

    @Test
    void testGetHelperInformation_HelperInformationNotFound() {
        Long helperInformationId = 1L;

        when(helperInformationRepository.findById(helperInformationId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> helperInformationUseCases.getHelperInformation(helperInformationId));
    }


    @Test
    void testGetHelperInformationByUser_Success() {
        Long userId = 1L;

        HelperInformation mockHelperInformation = new HelperInformation();
        mockHelperInformation.setId(1L);
        when(helperInformationRepository.findByUserId(userId)).thenReturn(Optional.of(mockHelperInformation));

        HelperInformation result = helperInformationUseCases.getHelperInformationByUser(userId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetHelperInformationByUser_HelperInformationNotFound() {
        Long userId = 1L;

        when(helperInformationRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> helperInformationUseCases.getHelperInformationByUser(userId));
    }

    @Test
    void testGetAllHelperInformation_Success() {
        HelperInformation helper1 = new HelperInformation();
        helper1.setId(1L);
        HelperInformation helper2 = new HelperInformation();
        helper2.setId(2L);

        List<HelperInformation> mockHelperList = Arrays.asList(helper1, helper2);
        when(helperInformationRepository.findAll()).thenReturn(mockHelperList);

        List<HelperInformation> result = helperInformationUseCases.getAllHelperInformation();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetAllHelperInformation_EmptyList() {
        when(helperInformationRepository.findAll()).thenReturn(Collections.emptyList());

        List<HelperInformation> result = helperInformationUseCases.getAllHelperInformation();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }



}