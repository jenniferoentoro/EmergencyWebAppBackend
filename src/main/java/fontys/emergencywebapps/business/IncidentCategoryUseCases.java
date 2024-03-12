package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.IncidentCategoryDto;
import fontys.emergencywebapps.controllers.dto.IncidentCategoryRequest;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;

import java.util.List;

public interface IncidentCategoryUseCases {

    IncidentCategory save(IncidentCategoryRequest incidentCategory);
    Iterable<IncidentCategory> findAll();
    void deleteById(Long id);
    IncidentCategory findById(Long id);



    IncidentCategory update(IncidentCategoryDto incidentCategory);

    void deleteIncidentCategories(List<Long> ids);
}
