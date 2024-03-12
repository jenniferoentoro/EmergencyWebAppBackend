package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.controllers.dto.IncidentCategoryDto;
import fontys.emergencywebapps.controllers.dto.IncidentCategoryRequest;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.IncidentCategoryUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class IncidentCategoryServiceImpl implements IncidentCategoryUseCases {
    private final IncidentCategoryRepository incidentCategoryRepository;

    private final ModelMapper modelMapper;

    private static final String EXIST_MESSAGE = "Incident Category already exists";

    private static final String CATEGORY_NOT_FOUND = "Incident Category not found";

    @Override
    public IncidentCategory save(IncidentCategoryRequest incidentCategory) {
        IncidentCategory existingCategory = incidentCategoryRepository.findByName(incidentCategory.getName());
        if (existingCategory != null) {
            throw new CustomException(EXIST_MESSAGE);

        }

        return incidentCategoryRepository.save(modelMapper.map(incidentCategory, IncidentCategory.class));
    }

    @Override
    public Iterable<IncidentCategory> findAll() {

        return incidentCategoryRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(id);
        if (incidentCategory.isEmpty()) {
            throw new CustomException(CATEGORY_NOT_FOUND);
        }
        incidentCategoryRepository.deleteById(id);


    }

    @Override
    public IncidentCategory findById(Long id) {
        Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(id);
        if (incidentCategory.isEmpty()) {
            throw new CustomException(CATEGORY_NOT_FOUND);
        }
        return incidentCategory.get();
    }


    @Override
    public IncidentCategory update(IncidentCategoryDto incidentCategory) {
        IncidentCategory existingCategory = incidentCategoryRepository.findByName(incidentCategory.getName());
        if (existingCategory != null) {
            throw new CustomException(EXIST_MESSAGE);

        }
        Optional<IncidentCategory> incidentCategoryOptional = incidentCategoryRepository.findById(incidentCategory.getId());
        if (incidentCategoryOptional.isEmpty()) {
            throw new CustomException(EXIST_MESSAGE);
        }

        incidentCategoryOptional.get().setName(incidentCategory.getName());

        return incidentCategoryRepository.save(incidentCategoryOptional.get());
    }

    @Override
    public void deleteIncidentCategories(List<Long> ids) {
        for (Long id : ids) {
            Optional<IncidentCategory> incidentCategory = incidentCategoryRepository.findById(id);
            if (incidentCategory.isEmpty()) {
                throw new CustomException("Not all Incident Categories are found");
            }

        }


        incidentCategoryRepository.deleteAllByIdIn(ids);


    }
}
