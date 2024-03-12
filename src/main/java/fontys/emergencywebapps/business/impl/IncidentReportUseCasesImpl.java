package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.IncidentReportUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.configurations.security.token.AccessToken;
import fontys.emergencywebapps.configurations.security.token.AccessTokenDecoder;
import fontys.emergencywebapps.controllers.dto.IncidentReportRequest;
import fontys.emergencywebapps.persistence.entities.*;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.IncidentReportRepository;
import fontys.emergencywebapps.persistence.repos.IncidentReportStatusRepository;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class IncidentReportUseCasesImpl implements IncidentReportUseCases {
    private final IncidentReportRepository incidentReportRepository;
    private final UserRepository userRepository;
    private final IncidentCategoryRepository incidentCategoryRepository;
    private final AccessTokenDecoder jwtService;


    private final IncidentReportStatusRepository incidentReportStatusRepository;

    @Override
    public Iterable<IncidentReport> findAllByMe(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);
        return incidentReportRepository.findAllByUserIdOrderByIdDesc(claims.getUserId());
    }

    @Override
    public IncidentReport save(IncidentReportRequest incidentReportRequest, HttpServletRequest servletRequest) throws IllegalStateException, IOException {

        String token = servletRequest.getHeader("Authorization").substring(7);
        AccessToken claims = jwtService.decode(token);

//        check file size
        if (incidentReportRequest.getFile().getSize() > 3000000) {
            throw new CustomException("File size is too large");
        }


        UUID uuid = UUID.randomUUID();
        String fileExtension = "";

        if (incidentReportRequest.getFile() == null || incidentReportRequest.getFile().getOriginalFilename() == null) {
            throw new CustomException("File extension is not valid");
        }

        String originalFilename = incidentReportRequest.getFile().getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            String[] splitFileName = originalFilename.split("\\.");
            if (splitFileName.length > 1) {
                fileExtension = splitFileName[1];
            }
        } else {
            throw new CustomException("File extension is not valid");
        }
        String fileName = uuid.toString() + "." + fileExtension;
        String uploadDirPath = "D:\\Kuliah\\Fontys\\Semester3\\Java\\Project\\emergency-web-apps-frontend\\public\\" + "incidentReportPhotos";
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        incidentReportRequest.getFile().transferTo(new File(uploadDirPath + File.separator + fileName));


        Optional<User> user = userRepository.findById(claims.getUserId());

        if (user.isEmpty()) {
            throw new CustomException("User not found");
        }

        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(incidentReportRequest.getIncidentCategoryId());
        if (incidentCategory.isEmpty()) {
            throw new CustomException("Incident Category not found");
        }
        IncidentReport incidentReport = IncidentReport.builder()
                .description(incidentReportRequest.getDescription())
                .incidentCategory(incidentCategory.get())
                .photoUrl(fileName)
                .longitude(incidentReportRequest.getLongitude())
                .latitude(incidentReportRequest.getLatitude())
                .title(incidentReportRequest.getTitle()).statusFixed(false)
                .build();

        incidentReport.setDate(new Date());
        incidentReport.setUser(user.get());
        IncidentReport savedIncidentReport = incidentReportRepository.save(incidentReport);


        incidentReportStatusRepository.save(IncidentReportStatus.builder()
                .incidentReport(savedIncidentReport)
                .status(Status.WAITING)
                .description("This incident report is waiting for the response")
                .date(new Date())
                .build());


        return savedIncidentReport;

    }

    @Override
    public IncidentReport findOne(Long id) {
        Optional<IncidentReport> incidentReportOptional = incidentReportRepository.findById(id);
        if (incidentReportOptional.isEmpty()) {
            throw new CustomException("Incident Report Not Found");
        }
        return incidentReportOptional.get();
    }


    @Override
    public Iterable<IncidentReport> findAll() {
        return incidentReportRepository.findAll();
    }

    @Override
    public Iterable<IncidentReport> findAllByUserId(Long id) {
        return incidentReportRepository.findAllByUserId(id);
    }

    @Override
    public Iterable<IncidentReport> findAllByIncidentCategoryId(Long id) {

        return incidentReportRepository.findAllByIncidentCategoryId(id);
    }

    @Override
    public Iterable<IncidentReport> findAllByIncidentCategoryIdAndUserId(Long incidentCategoryId, Long userId) {
        return incidentReportRepository.findAllByIncidentCategoryIdAndUserId(incidentCategoryId, userId);
    }

    @Override
    public Iterable<IncidentReport> findInProgressReports() {
        return incidentReportRepository.findInProgressReports();
    }

    @Override
    public Iterable<IncidentReport> findFixedReports() {
        return incidentReportRepository.findFixedReports();
    }

    @Override
    public Iterable<IncidentReport> findWaitingReports() {
        return incidentReportRepository.findWaitingReports();
    }

    @Override
    public int countInProgressReports() {
        return incidentReportRepository.countInProgressReports();
    }

    @Override
    public int countFixedReports() {
        return incidentReportRepository.countFixedReports();
    }

    @Override
    public int countWaitingReports() {
        return incidentReportRepository.countWaitingReports();
    }

    @Override
    public int countStatusFixed() {
        return incidentReportRepository.countByStatusFixed(true);
    }

    @Override
    public int countStatusInProgress() {
        return incidentReportRepository.countByStatusFixed(false);
    }

    @Override
    public List<Object[]> countIncidentReportsByCategory() {
        return incidentReportRepository.countIncidentReportsByCategory();
    }

    @Override
    public List<Object[]> countIncidentReportsLast5Days() {
        return incidentReportRepository.countIncidentReportsLast5Days();
    }


}
