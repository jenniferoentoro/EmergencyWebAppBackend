package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.IncidentReportStatusRequest;
import fontys.emergencywebapps.controllers.dto.IncidentReportStatusUpdateRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReport;
import fontys.emergencywebapps.persistence.entities.IncidentReportStatus;
import fontys.emergencywebapps.persistence.repos.IncidentReportRepository;
import fontys.emergencywebapps.persistence.repos.IncidentReportStatusRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.IncidentReportStatusUseCases;
import fontys.emergencywebapps.persistence.entities.Status;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class IncidentReportStatusServiceImpl implements IncidentReportStatusUseCases {
    private final IncidentReportStatusRepository incidentReportStatusRepository;

    private final ModelMapper modelMapper;
    private final IncidentReportUseCasesImpl incidentReportService;

    private final IncidentReportRepository incidentReportRepository;
    private static final String NOT_FOUND = "Incident report status not found";


    @Override
    public IncidentReportStatus createIncidentReportStatus(IncidentReportStatusRequest incidentReportStatusRequest) {
        Long incidentReportId = incidentReportStatusRequest.getIncidentReportId();

        IncidentReport incidentReport = incidentReportService.findOne(incidentReportId);
        if (incidentReport == null) {
            throw new CustomException("Incident report not found");
        }

        IncidentReport incidentReportDto = incidentReportService.findOne(incidentReportStatusRequest.getIncidentReportId());

        if(Objects.equals(incidentReportStatusRequest.getStatus(), "FIXED")){
            incidentReportRepository.updateStatusById(incidentReportId, true);
        }

        return incidentReportStatusRepository.save(IncidentReportStatus.builder().incidentReport(modelMapper.map(incidentReportDto, IncidentReport.class)).status(Status.valueOf(incidentReportStatusRequest.getStatus())).description(incidentReportStatusRequest.getDescription()).date(new Date()).build());
    }


    @Override
    public IncidentReportStatus getIncidentReportStatus(Long id) {

        Optional<IncidentReportStatus> incidentReportStatus = incidentReportStatusRepository.findById(id);
        if (incidentReportStatus.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        return incidentReportStatus.get();
    }

    @Override
    public Iterable<IncidentReportStatus> getByIncidentReportId(Long id) {
        return incidentReportStatusRepository.findByIncidentReportId(id);

    }


    @Override
    public IncidentReportStatus updateIncidentReportStatus(IncidentReportStatusUpdateRequest incidentReportStatusRequest, Long id) {

        Optional<IncidentReportStatus> incidentReportStatus = incidentReportStatusRepository.findById(id);
        if (incidentReportStatus.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        incidentReportStatus.get().setDate(new Date());
        incidentReportStatus.get().setDescription(incidentReportStatusRequest.getDescription());
        incidentReportStatus.get().setStatus(Status.valueOf(incidentReportStatusRequest.getStatus()));

        return incidentReportStatusRepository.save(incidentReportStatus.get());
    }

    @Override
    public void deleteIncidentReportStatus(Long id) {
        Optional<IncidentReportStatus> incidentReportStatus = incidentReportStatusRepository.findById(id);
        if (incidentReportStatus.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }
        incidentReportStatusRepository.deleteById(id);
    }
}
