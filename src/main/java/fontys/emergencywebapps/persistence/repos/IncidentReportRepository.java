package fontys.emergencywebapps.persistence.repos;


import fontys.emergencywebapps.persistence.entities.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    Iterable<IncidentReport> findAllByUserId(Long id);
    Iterable<IncidentReport> findAllByUserIdOrderByIdDesc(Long id);
    Iterable<IncidentReport> findAllByIncidentCategoryId(Long id);
    Iterable<IncidentReport> findAllByIncidentCategoryIdAndUserId(Long incidentCategoryId, Long userId);

    @Query("SELECT ir FROM IncidentReport ir " +
            "JOIN IncidentReportStatus s on ir.id = s.incidentReport.id " +
            "WHERE s.status = 'IN_PROGRESS' AND ir.statusFixed = false " +
            "GROUP BY ir.id " +
            "ORDER BY MAX(s.date) DESC")
    List<IncidentReport> findInProgressReports();

    @Query("SELECT ir FROM IncidentReport ir " +
            "JOIN IncidentReportStatus s on ir.id = s.incidentReport.id " +
            "WHERE s.status = 'FIXED' AND ir.statusFixed = true " +
            "GROUP BY ir.id " +
            "ORDER BY MAX(s.date) DESC")
    List<IncidentReport> findFixedReports();

    @Query("SELECT ir FROM IncidentReport ir " +
            "JOIN IncidentReportStatus s on ir.id = s.incidentReport.id " +
            "WHERE s.status = 'WAITING' AND ir.statusFixed = false " +
            "AND NOT EXISTS (SELECT 1 FROM IncidentReportStatus s2 WHERE s2.incidentReport.id = ir.id AND s2.status = 'IN_PROGRESS') " +
            "GROUP BY ir.id " +
            "ORDER BY MAX(s.date) DESC")
    List<IncidentReport> findWaitingReports();
    @Modifying
    @Query("UPDATE IncidentReport ir SET ir.statusFixed = :statusFixed WHERE ir.id = :incidentReportId")
    int updateStatusById(@Param("incidentReportId") Long incidentReportId, @Param("statusFixed") boolean statusFixed);


    @Query("SELECT COUNT(DISTINCT ir.id) FROM IncidentReport ir " +
            "JOIN IncidentReportStatus s on ir.id = s.incidentReport.id " +
            "WHERE s.status = 'IN_PROGRESS' AND ir.statusFixed = false ")
    int countInProgressReports();

    @Query("SELECT COUNT(DISTINCT ir.id) FROM IncidentReport ir " +
            "JOIN IncidentReportStatus s on ir.id = s.incidentReport.id " +
            "WHERE s.status = 'FIXED' AND ir.statusFixed = true ")
    int countFixedReports();

    @Query("SELECT COUNT(DISTINCT ir.id) FROM IncidentReport ir " +
            "JOIN IncidentReportStatus s on ir.id = s.incidentReport.id " +
            "WHERE s.status = 'WAITING' AND ir.statusFixed = false " +
            "AND NOT EXISTS (SELECT 1 FROM IncidentReportStatus s2 WHERE s2.incidentReport.id = ir.id AND s2.status = 'IN_PROGRESS') ")
    int countWaitingReports();


    int countByStatusFixed(boolean statusFixed);

    @Query("SELECT c.name, COUNT(ir.incidentCategory.id) FROM IncidentCategory c " +
            "JOIN IncidentReport ir ON c.id = ir.incidentCategory.id " +
            "GROUP BY ir.incidentCategory.id")
    List<Object[]> countIncidentReportsByCategory();

    @Query("SELECT DATE(ir.date) AS incidentDay, COUNT(ir.id) AS totalIncidents " +
            "FROM IncidentReport ir " +
            "WHERE ir.date >= CURRENT_DATE - 4 " +
            "GROUP BY incidentDay " +
            "ORDER BY 1 ASC")
    List<Object[]> countIncidentReportsLast5Days();


}
