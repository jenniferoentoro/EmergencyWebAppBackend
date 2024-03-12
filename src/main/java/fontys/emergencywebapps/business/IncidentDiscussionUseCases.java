package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionUpdateRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReportDiscussion;
import jakarta.servlet.http.HttpServletRequest;
import fontys.emergencywebapps.controllers.dto.IncidentReportDiscussionRequest;

public interface IncidentDiscussionUseCases {
    IncidentReportDiscussion getIncidentReportDiscussion(Long id);
    IncidentReportDiscussion createIncidentReportDiscussion(IncidentReportDiscussionRequest incidentReportDiscussionRequest, HttpServletRequest request);
    IncidentReportDiscussion updateIncidentReportDiscussion(Long id, IncidentReportDiscussionUpdateRequest incidentReportDiscussionRequest, HttpServletRequest request);

    void  deleteIncidentReportDiscussion(Long id);

    Iterable<IncidentReportDiscussion> getAllIncidentReportDiscussion();

    Iterable<IncidentReportDiscussion> getAllIncidentReportDiscussionByIncidentReportId(Long id);


}
