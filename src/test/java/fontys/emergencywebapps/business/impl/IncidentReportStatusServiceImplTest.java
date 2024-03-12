package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.IncidentReportStatusRequest;
import fontys.emergencywebapps.controllers.dto.IncidentReportStatusUpdateRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReport;
import fontys.emergencywebapps.persistence.entities.IncidentReportStatus;
import fontys.emergencywebapps.persistence.entities.Status;
import fontys.emergencywebapps.persistence.repos.IncidentReportStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {IncidentReportStatusServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class IncidentReportStatusServiceImplTest {
    @Mock
    private IncidentReportStatusRepository incidentReportStatusRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private IncidentReportUseCasesImpl incidentReportService;

    @InjectMocks
    private IncidentReportStatusServiceImpl incidentReportStatusService;


    @Test
    void testCreateIncidentReportStatus_IncidentReportNotFound_ShouldThrowCustomException() {
        Long incidentReportId = 1L;
        String status = "WAITING";
        String description = "Test status description";

        IncidentReportStatusRequest request = new IncidentReportStatusRequest();
        request.setIncidentReportId(incidentReportId);
        request.setStatus(status);
        request.setDescription(description);

        when(incidentReportService.findOne(incidentReportId)).thenReturn(null);

        assertThrows(CustomException.class, () -> incidentReportStatusService.createIncidentReportStatus(request));

        verify(incidentReportService, times(1)).findOne(incidentReportId);
        verify(modelMapper, times(0)).map(any(), any());
        verify(incidentReportStatusRepository, times(0)).save(any(IncidentReportStatus.class));
    }

    @Test
    void testGetIncidentReportStatus_ValidId_ShouldReturnStatus() {
        Long statusId = 1L;
        IncidentReportStatus mockStatus = new IncidentReportStatus();
        mockStatus.setId(statusId);

        when(incidentReportStatusRepository.findById(statusId)).thenReturn(Optional.of(mockStatus));

        IncidentReportStatus result = incidentReportStatusService.getIncidentReportStatus(statusId);

        assertNotNull(result);
        assertEquals(statusId, result.getId());
        verify(incidentReportStatusRepository, times(1)).findById(statusId);
    }

    @Test
    void testGetIncidentReportStatus_InvalidId_ShouldThrowException() {
        Long statusId = 1L;

        when(incidentReportStatusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportStatusService.getIncidentReportStatus(statusId));
        verify(incidentReportStatusRepository, times(1)).findById(statusId);
    }

    @Test
    void testGetByIncidentReportId_ValidId_ShouldReturnStatusList() {
        Long incidentReportId = 1L;
        List<IncidentReportStatus> mockStatusList = Arrays.asList(new IncidentReportStatus(), new IncidentReportStatus());

        when(incidentReportStatusRepository.findByIncidentReportId(incidentReportId)).thenReturn(mockStatusList);

        Iterable<IncidentReportStatus> result = incidentReportStatusService.getByIncidentReportId(incidentReportId);

        assertNotNull(result);
        assertEquals(mockStatusList.size(), ((Collection<?>) result).size());
        verify(incidentReportStatusRepository, times(1)).findByIncidentReportId(incidentReportId);
    }

    @Test
    void testUpdateIncidentReportStatus_ValidRequest_ShouldUpdateStatus() {
        Long statusId = 1L;
        IncidentReportStatusUpdateRequest request = new IncidentReportStatusUpdateRequest();
        request.setStatus("IN_PROGRESS");
        request.setDescription("Updated description");

        IncidentReportStatus mockStatus = new IncidentReportStatus();
        mockStatus.setId(statusId);

        when(incidentReportStatusRepository.findById(statusId)).thenReturn(Optional.of(mockStatus));
        when(incidentReportStatusRepository.save(any(IncidentReportStatus.class))).thenReturn(mockStatus);

        IncidentReportStatus result = incidentReportStatusService.updateIncidentReportStatus(request, statusId);

        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus().toString());
        assertEquals("Updated description", result.getDescription());
        verify(incidentReportStatusRepository, times(1)).findById(statusId);
        verify(incidentReportStatusRepository, times(1)).save(any(IncidentReportStatus.class));
    }


    @Test
    void testUpdateIncidentReportStatus_InvalidId_ShouldThrowException() {
        Long statusId = 1L;
        IncidentReportStatusUpdateRequest request = new IncidentReportStatusUpdateRequest();
        request.setStatus("IN_PROGRESS");
        request.setDescription("Updated description");

        when(incidentReportStatusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportStatusService.updateIncidentReportStatus(request, statusId));
        verify(incidentReportStatusRepository, times(1)).findById(statusId);
        verify(incidentReportStatusRepository, never()).save(any(IncidentReportStatus.class));
    }

    @Test
    void testCreateIncidentReportStatus_ValidRequest_ShouldCreateStatus() {
        Long incidentReportId = 1L;
        String status = "WAITING";
        String description = "Test status description";

        IncidentReportStatusRequest request = new IncidentReportStatusRequest();
        request.setIncidentReportId(incidentReportId);
        request.setStatus(status);
        request.setDescription(description);

        IncidentReport mockIncidentReport = new IncidentReport();
        mockIncidentReport.setId(incidentReportId);

        when(incidentReportService.findOne(incidentReportId)).thenReturn(mockIncidentReport);

        when(modelMapper.map(any(), eq(IncidentReport.class))).thenReturn(mockIncidentReport);

        IncidentReportStatus savedStatus = new IncidentReportStatus();
        savedStatus.setId(1L);
        when(incidentReportStatusRepository.save(any(IncidentReportStatus.class))).thenReturn(savedStatus);

        IncidentReportStatus result = incidentReportStatusService.createIncidentReportStatus(request);

        assertNotNull(result);

        verify(incidentReportService, times(2)).findOne(incidentReportId);
        verify(modelMapper, times(1)).map(any(), eq(IncidentReport.class));
        verify(incidentReportStatusRepository, times(1)).save(any(IncidentReportStatus.class));
    }


    @Test
    void testDeleteIncidentReportStatus_ValidId_ShouldDeleteStatus() {
        Long statusId = 1L;
        IncidentReportStatus mockStatus = new IncidentReportStatus();
        mockStatus.setId(statusId);

        when(incidentReportStatusRepository.findById(statusId)).thenReturn(Optional.of(mockStatus));

        incidentReportStatusService.deleteIncidentReportStatus(statusId);

        verify(incidentReportStatusRepository, times(1)).findById(statusId);
        verify(incidentReportStatusRepository, times(1)).deleteById(statusId);
    }

    @Test
    void testDeleteIncidentReportStatus_InvalidId_ShouldThrowException() {
        Long statusId = 1L;

        when(incidentReportStatusRepository.findById(statusId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentReportStatusService.deleteIncidentReportStatus(statusId));
        verify(incidentReportStatusRepository, times(1)).findById(statusId);
        verify(incidentReportStatusRepository, never()).deleteById(statusId);
    }
}