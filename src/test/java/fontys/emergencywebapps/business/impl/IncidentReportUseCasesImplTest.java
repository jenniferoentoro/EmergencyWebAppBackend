package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.configurations.security.token.impl.AccessTokenImpl;
import fontys.emergencywebapps.controllers.dto.IncidentReportRequest;
import fontys.emergencywebapps.persistence.entities.*;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.IncidentReportRepository;
import fontys.emergencywebapps.persistence.repos.IncidentReportStatusRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {IncidentReportUseCasesImpl.class})
class IncidentReportUseCasesImplTest {
    @InjectMocks
    private IncidentReportUseCasesImpl incidentReportUseCases;

    @Mock
    private IncidentReportRepository incidentReportRepository;

    @Mock
    private IncidentReportStatusRepository incidentReportStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IncidentCategoryRepository incidentCategoryRepository;


    @Mock
    private AccessTokenDecoder jwtService;

    @Test
    void testFindAllByMe_UserAuthenticated_Success() {
        String token = "TOKEN_MOCK";
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("Authorization", token);

        AccessTokenImpl mockAccessToken = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode(token.substring(7))).thenReturn(mockAccessToken);
        List<IncidentReport> mockIncidentReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findAllByUserIdOrderByIdDesc(mockAccessToken.getUserId())).thenReturn(mockIncidentReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findAllByMe(servletRequest);

        assertNotNull(result);
        assertEquals(mockIncidentReports, result);
    }



    @Test
    void testFindOne_IncidentReportFound_Success() {
        Long incidentReportId = 1L;
        IncidentReport mockIncidentReport = new IncidentReport();
        when(incidentReportRepository.findById(incidentReportId)).thenReturn(Optional.of(mockIncidentReport));

        IncidentReport result = incidentReportUseCases.findOne(incidentReportId);

        assertNotNull(result);
        assertEquals(mockIncidentReport, result);
    }

    @Test
    void testFindOne_IncidentReportNotFound_Exception() {
        Long nonExistingIncidentReportId = 999L;
        when(incidentReportRepository.findById(nonExistingIncidentReportId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> {
            incidentReportUseCases.findOne(nonExistingIncidentReportId);
        });

    }

    @Test
    void testFindAll_Success() {
        List<IncidentReport> mockIncidentReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findAll()).thenReturn(mockIncidentReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findAll();

        assertNotNull(result);
        assertEquals(mockIncidentReports, result);
    }

    @Test
    void testFindAllByUserId_Success() {
        Long userId = 1L;
        List<IncidentReport> mockIncidentReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findAllByUserId(userId)).thenReturn(mockIncidentReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findAllByUserId(userId);

        assertNotNull(result);
        assertEquals(mockIncidentReports, result);
    }

    @Test
    void testFindAllByIncidentCategoryId_Success() {
        Long incidentCategoryId = 1L;
        List<IncidentReport> mockIncidentReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findAllByIncidentCategoryId(incidentCategoryId)).thenReturn(mockIncidentReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findAllByIncidentCategoryId(incidentCategoryId);

        assertNotNull(result);
        assertEquals(mockIncidentReports, result);
    }

    @Test
    void testFindAllByIncidentCategoryIdAndUserId_Success() {
        Long incidentCategoryId = 1L;
        Long userId = 2L;
        List<IncidentReport> mockIncidentReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findAllByIncidentCategoryIdAndUserId(incidentCategoryId, userId))
                .thenReturn(mockIncidentReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findAllByIncidentCategoryIdAndUserId(incidentCategoryId, userId);

        assertNotNull(result);
        assertEquals(mockIncidentReports, result);
    }

    @Test
    void testFindWaitingReports_Success() {
        List<IncidentReport> mockWaitingReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findWaitingReports()).thenReturn(mockWaitingReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findWaitingReports();

        assertNotNull(result);
        assertEquals(mockWaitingReports, result);
    }

    @Test
    void testFindInProgressReports_Success() {
        List<IncidentReport> mockInProgressReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findInProgressReports()).thenReturn(mockInProgressReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findInProgressReports();

        assertNotNull(result);
        assertEquals(mockInProgressReports, result);
    }

    @Test
    void testFindFixedReports_Success() {
        List<IncidentReport> mockFixedReports = Arrays.asList(
                new IncidentReport(), new IncidentReport(), new IncidentReport()
        );
        when(incidentReportRepository.findFixedReports()).thenReturn(mockFixedReports);

        Iterable<IncidentReport> result = incidentReportUseCases.findFixedReports();

        assertNotNull(result);
        assertEquals(mockFixedReports, result);
    }

    @Test
    void testCountInProgressReports_Success() {
        int mockCountInProgressReports = 3;
        when(incidentReportRepository.countInProgressReports()).thenReturn(mockCountInProgressReports);

        int result = incidentReportUseCases.countInProgressReports();

        assertEquals(mockCountInProgressReports, result);
    }

    @Test
    void testCountFixedReports_Success() {
        int mockCountFixedReports = 3;
        when(incidentReportRepository.countFixedReports()).thenReturn(mockCountFixedReports);

        int result = incidentReportUseCases.countFixedReports();

        assertEquals(mockCountFixedReports, result);
    }

    @Test
    void testCountWaitingReports_Success() {
        int mockCountWaitingReports = 3;
        when(incidentReportRepository.countWaitingReports()).thenReturn(mockCountWaitingReports);

        int result = incidentReportUseCases.countWaitingReports();

        assertEquals(mockCountWaitingReports, result);
    }

    @Test
    void testCountStatusFixed_Success() {
        int mockCountStatusFixed = 3;
        when(incidentReportRepository.countByStatusFixed(true)).thenReturn(mockCountStatusFixed);

        int result = incidentReportUseCases.countStatusFixed();

        assertEquals(mockCountStatusFixed, result);
    }


    @Test
    void testCountStatusInProgress_Success() {
        int mockCountStatusInProgress = 3;
        when(incidentReportRepository.countByStatusFixed(false)).thenReturn(mockCountStatusInProgress);

        int result = incidentReportUseCases.countStatusInProgress();

        assertEquals(mockCountStatusInProgress, result);
    }

    @Test
    void testCountIncidentReportsByCategory_Success() {
        List<Object[]> mockCountIncidentReportsByCategory = Arrays.asList(
                new Object[]{1L, 2L}, new Object[]{2L, 3L}, new Object[]{3L, 4L}
        );
        when(incidentReportRepository.countIncidentReportsByCategory()).thenReturn(mockCountIncidentReportsByCategory);

        List<Object[]> result = incidentReportUseCases.countIncidentReportsByCategory();

        assertNotNull(result);
        assertEquals(mockCountIncidentReportsByCategory, result);
    }

    @Test
    void testCountIncidentReportsLast5Days_Success() {
        List<Object[]> mockCountIncidentReportsLast5Days = Arrays.asList(
                new Object[]{1L, 2L}, new Object[]{2L, 3L}, new Object[]{3L, 4L}
        );
        when(incidentReportRepository.countIncidentReportsLast5Days()).thenReturn(mockCountIncidentReportsLast5Days);

        List<Object[]> result = incidentReportUseCases.countIncidentReportsLast5Days();

        assertNotNull(result);
        assertEquals(mockCountIncidentReportsLast5Days, result);
    }

    @Test
    void testSave_Successful() throws IOException {
        IncidentReportRequest incidentReportRequest = new IncidentReportRequest();
        incidentReportRequest.setDescription("Test Description");
        incidentReportRequest.setIncidentCategoryId(1L);
        incidentReportRequest.setLongitude(1.0);
        incidentReportRequest.setLatitude(2.0);
        incidentReportRequest.setTitle("Test Title");

        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "mockFile".getBytes());
        incidentReportRequest.setFile(mockFile);

        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer mockToken");

        AccessTokenImpl mockAccessToken = new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER"));
        when(jwtService.decode("mockToken")).thenReturn(mockAccessToken);

        User mockUser = new User();
        mockUser.setId(1L);
        when(userRepository.findById(mockAccessToken.getUserId())).thenReturn(Optional.of(mockUser));

        IncidentCategory mockIncidentCategory = new IncidentCategory();
        mockIncidentCategory.setId(1L);
        when(incidentCategoryRepository.findById(incidentReportRequest.getIncidentCategoryId())).thenReturn(Optional.of(mockIncidentCategory));

        IncidentReport mockIncidentReport = new IncidentReport();
        mockIncidentReport.setId(1L);
        when(incidentReportRepository.save(any(IncidentReport.class))).thenReturn(mockIncidentReport);

        IncidentReportStatus mockIncidentReportStatus = new IncidentReportStatus();
        mockIncidentReportStatus.setId(1L);
        when(incidentReportStatusRepository.save(any(IncidentReportStatus.class))).thenReturn(mockIncidentReportStatus);

        IncidentReport result = incidentReportUseCases.save(incidentReportRequest, servletRequest);

        assertNotNull(result);
        assertEquals(mockIncidentReport, result);
    }

    @Test
    void testSave_UserNotFound() throws IOException {
        // Test when the user is not found in the repository
        IncidentReportRequest incidentReportRequest = createMockIncidentReportRequest();
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer mockToken");

        when(jwtService.decode("mockToken")).thenReturn(new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER")));

        when(userRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportUseCases.save(incidentReportRequest, servletRequest));
    }

    @Test
    void testSave_IncidentCategoryNotFound() throws IOException {
        // Test when the incident category is not found in the repository
        IncidentReportRequest incidentReportRequest = createMockIncidentReportRequest();
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer mockToken");

        when(jwtService.decode("mockToken")).thenReturn(new AccessTokenImpl("mockToken", 1L, Collections.singleton("USER")));

        when(userRepository.findById(1l)).thenReturn(Optional.of(new User()));
        when(incidentCategoryRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportUseCases.save(incidentReportRequest, servletRequest));
    }

//    @Test
//    void testSave_SuccessfulWithMinimumFileSize() throws IOException {
//        // Test when the file size is exactly 3000000 bytes (minimum allowed size)
//        IncidentReportRequest incidentReportRequest = createMockIncidentReportRequest();
//        incidentReportRequest.getFile().getBytes()[0] = new byte[3000000]; // set file size to 3000000 bytes
//        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//        when(servletRequest.getHeader("Authorization")).thenReturn("Bearer mockToken");
//
//        when(jwtService.decode("mockToken")).thenReturn(new AccessToken("mockToken", 1L, null));
//
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
//        when(incidentCategoryRepository.findById(anyLong())).thenReturn(Optional.of(new IncidentCategory()));
//        when(incidentReportRepository.save(any(IncidentReport.class))).thenReturn(new IncidentReport());
//
//        assertNotNull(incidentReportUseCases.save(incidentReportRequest, servletRequest));
//    }

    // Add more test methods for other scenarios if needed

    private IncidentReportRequest createMockIncidentReportRequest() {
        IncidentReportRequest incidentReportRequest = new IncidentReportRequest();
        incidentReportRequest.setDescription("Test Description");
        incidentReportRequest.setIncidentCategoryId(1L);
        incidentReportRequest.setLongitude(1.0);
        incidentReportRequest.setLatitude(2.0);
        incidentReportRequest.setTitle("Test Title");
        incidentReportRequest.setFile(new MockMultipartFile("file", "test.jpg", "image/jpeg", "mockFile".getBytes()));
        return incidentReportRequest;
    }


}