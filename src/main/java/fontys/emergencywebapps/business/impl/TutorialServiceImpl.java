package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.TutorialRequest;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import fontys.emergencywebapps.persistence.entities.Tutorial;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import fontys.emergencywebapps.persistence.repos.TutorialRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.TutorialUseCases;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class TutorialServiceImpl implements TutorialUseCases {
    private final IncidentCategoryRepository incidentCategoryRepository;
    private final TutorialRepository tutorialRepository;
    private static final String PATH = "D:\\Kuliah\\Fontys\\Semester3\\Java\\Project\\emergency-web-apps-frontend\\public\\videoTutorials";
    private static final String FILE_EXTENSION_NOT_VALID = "File extension is not valid";

    private static final String NOT_FOUND = "Tutorial not found";

    @Override
    public Tutorial createTutorial(TutorialRequest request) throws IOException {
        String fileName = uploadVideoFile(request.getVideoFile());
        String link = request.getLinkToVideo();
        IncidentCategory incidentCategory = validateIncidentCategory(request.getIncidentCategory());
        Tutorial tutorial = createTutorialEntity(request, fileName, link, incidentCategory);
        return tutorialRepository.save(tutorial);
    }

    private String uploadVideoFile(MultipartFile videoFile) throws IOException {
        if (videoFile == null) {
            return null;
        }
        long fileSize = videoFile.getSize();
        if (fileSize > 3000000) {
            throw new CustomException("File size is too large");
        }

        UUID uuid = UUID.randomUUID();
        String originalFilename = videoFile.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new CustomException(FILE_EXTENSION_NOT_VALID);
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = uuid.toString() + fileExtension;

        String uploadDirPath = PATH;
        File uploadDir = new File(uploadDirPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        videoFile.transferTo(new File(uploadDirPath + File.separator + fileName));

        return fileName;
    }

    private IncidentCategory validateIncidentCategory(Long incidentCategoryId) {
        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(incidentCategoryId);
        if (incidentCategory.isEmpty()) {
            throw new CustomException("Incident Category not found");
        }
        return incidentCategory.get();
    }

    private Tutorial createTutorialEntity(TutorialRequest request, String fileName, String link, IncidentCategory incidentCategory) {
        return Tutorial.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .videoFile(fileName)
                .incidentCategory(incidentCategory)
                .linkToVideo(link)
                .build();
    }



    public Tutorial updateTutorial(Long id, TutorialRequest request) throws IOException {
        Optional<Tutorial> tutorialResponse = tutorialRepository.findById(id);
        if (tutorialResponse.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }

        String fileName = handleVideoFile(request);
        String link = request.getLinkToVideo();
        IncidentCategory incidentCategory = getIncidentCategory(request);

        Tutorial tutorial = tutorialResponse.get();
        tutorial.setTitle(request.getTitle());
        tutorial.setDescription(request.getDescription());
        tutorial.setVideoFile(fileName);
        tutorial.setIncidentCategory(incidentCategory);
        tutorial.setLinkToVideo(link);

        return tutorialRepository.save(tutorial);
    }

    public String handleVideoFile(TutorialRequest request) throws IOException {
        String fileName = null;
        MultipartFile videoFile = request.getVideoFile();
        if (videoFile != null) {
            if (videoFile.getSize() > 3000000) {
                throw new CustomException("Video size is too large");
            }

            fileName = saveVideoFile(videoFile);
        }
        return fileName;
    }

    public String saveVideoFile(MultipartFile videoFile) throws IOException {
        String originalFilename = videoFile.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new CustomException(FILE_EXTENSION_NOT_VALID);
        }

        UUID uuid = UUID.randomUUID();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        String fileName = uuid.toString() + "." + fileExtension;

        String uploadDirPath = PATH;
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        videoFile.transferTo(new File(uploadDirPath + File.separator + fileName));
        return fileName;
    }

    public IncidentCategory getIncidentCategory(TutorialRequest request) {
        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(request.getIncidentCategory());
        if (incidentCategory.isEmpty()) {
            throw new CustomException("Incident Category not found");
        }
        return incidentCategory.get();
    }


    @Override
    public void deleteTutorial(Long id) {

        Optional<Tutorial> tutorialResponse = tutorialRepository.findById(id);

        if (tutorialResponse.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }

        tutorialRepository.deleteById(id);

    }

    @Override
    public Tutorial getTutorial(Long id) {
        Optional<Tutorial> tutorialResponse = tutorialRepository.findById(id);

        if (tutorialResponse.isEmpty()) {
            throw new CustomException(NOT_FOUND);
        }

        return tutorialResponse.get();
    }

    @Override
    public Iterable<Tutorial> getTutorials() {

        return tutorialRepository.findAll();
    }


    @Override
    public Iterable<Tutorial> getTutorialByTitle(String incidentCategoryName) {
        return tutorialRepository.findAllByTitleContaining(incidentCategoryName);
    }

}
