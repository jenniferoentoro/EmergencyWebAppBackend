package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.TutorialRequest;
import fontys.emergencywebapps.persistence.entities.Tutorial;

import java.io.IOException;

public interface TutorialUseCases {
    Tutorial createTutorial(TutorialRequest request) throws IOException;

    Tutorial updateTutorial(Long id, TutorialRequest request) throws IOException;

    void deleteTutorial(Long id);

    Tutorial getTutorial(Long id);

    Iterable<Tutorial> getTutorials();

    Iterable<Tutorial> getTutorialByTitle(String incidentCategoryName);
}
