package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.IncidentReportStatusRequest;
import fontys.emergencywebapps.controllers.dto.IncidentReportStatusUpdateRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReportStatus;

public interface IncidentReportStatusUseCases {
   IncidentReportStatus createIncidentReportStatus(IncidentReportStatusRequest incidentReportStatusRequest);

   IncidentReportStatus getIncidentReportStatus(Long id);

 Iterable<IncidentReportStatus>  getByIncidentReportId(Long id);


    IncidentReportStatus updateIncidentReportStatus(IncidentReportStatusUpdateRequest incidentReportStatusRequest, Long id);

    void deleteIncidentReportStatus(Long id);

}
