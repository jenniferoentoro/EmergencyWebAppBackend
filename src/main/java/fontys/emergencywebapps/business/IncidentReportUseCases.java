package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.IncidentReportRequest;
import fontys.emergencywebapps.persistence.entities.IncidentReport;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface IncidentReportUseCases {

    Iterable<IncidentReport> findAllByMe(HttpServletRequest servletRequest);

    IncidentReport save(IncidentReportRequest incidentReportRequest, HttpServletRequest servletRequest) throws IOException;

    IncidentReport findOne(Long id);

    Iterable<IncidentReport> findAll();

    Iterable<IncidentReport> findAllByUserId(Long id);

    Iterable<IncidentReport> findAllByIncidentCategoryId(Long id);

    Iterable<IncidentReport> findAllByIncidentCategoryIdAndUserId(Long incidentCategoryId, Long userId);


    Iterable<IncidentReport> findInProgressReports();

    Iterable<IncidentReport> findFixedReports();

    Iterable<IncidentReport> findWaitingReports();


    int countInProgressReports();

    int countFixedReports();

    int countWaitingReports();

    int countStatusFixed();

    int countStatusInProgress();

    List<Object[]> countIncidentReportsByCategory();


    List<Object[]> countIncidentReportsLast5Days();
}
