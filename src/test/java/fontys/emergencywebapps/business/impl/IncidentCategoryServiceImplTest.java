package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.controllers.dto.IncidentCategoryRequest;
import fontys.emergencywebapps.persistence.repos.IncidentCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.controllers.dto.IncidentCategoryDto;
import fontys.emergencywebapps.persistence.entities.IncidentCategory;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {IncidentCategoryServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class IncidentCategoryServiceImplTest {

    @Mock
    IncidentCategoryRepository incidentCategoryRepository;
    @InjectMocks
    IncidentCategoryServiceImpl incidentCategoryService;
    @Mock
    ModelMapper modelMapper;

    @Test
    void testSave_SuccessfulCategoryCreation() {
        IncidentCategoryRequest request = new IncidentCategoryRequest();
        request.setName("NewCategoryName");

        when(incidentCategoryRepository.findByName(request.getName())).thenReturn(null);

        IncidentCategory mappedCategory = new IncidentCategory();
        when(modelMapper.map(request, IncidentCategory.class)).thenReturn(mappedCategory);

        when(incidentCategoryRepository.save(mappedCategory)).thenReturn(mappedCategory);

        IncidentCategory createdCategory = incidentCategoryService.save(request);

        assertNotNull(createdCategory);
        verify(incidentCategoryRepository).findByName(request.getName());
        verify(modelMapper).map(request, IncidentCategory.class);
        verify(incidentCategoryRepository).save(mappedCategory);
    }

    @Test
    void testSave_DuplicateCategoryName_ShouldThrowCustomException() {
        IncidentCategoryRequest request = new IncidentCategoryRequest();
        request.setName("ExistingCategoryName");

        IncidentCategory existingCategory = new IncidentCategory();
        when(incidentCategoryRepository.findByName(request.getName())).thenReturn(existingCategory);

        assertThrows(CustomException.class, () -> incidentCategoryService.save(request));
        verify(incidentCategoryRepository).findByName(request.getName());
        verify(modelMapper, never()).map(any(), eq(IncidentCategory.class));
        verify(incidentCategoryRepository, never()).save(any(IncidentCategory.class));
    }

    @Test
     void testFindAll() {
        List<IncidentCategory> categories = new ArrayList<>();
        categories.add(new IncidentCategory(1L, "Category 1"));
        categories.add(new IncidentCategory(2L, "Category 2"));

        when(incidentCategoryRepository.findAll()).thenReturn(categories);

        Iterable<IncidentCategory> result = incidentCategoryService.findAll();

        assertEquals(categories, result);
    }

    @Test
     void testDeleteById_CategoryFound() {
        Long categoryId = 1L;
        IncidentCategory category = new IncidentCategory(categoryId, "Category 1");

        when(incidentCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        incidentCategoryService.deleteById(categoryId);
        verify(incidentCategoryRepository).deleteById(categoryId);
    }

    @Test
     void testDeleteById_CategoryNotFound() {
        Long categoryId = 1L;

        when(incidentCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentCategoryService.deleteById(categoryId));
    }

    @Test
     void testFindById_CategoryFound() {
        Long categoryId = 1L;
        IncidentCategory category = new IncidentCategory(categoryId, "Category 1");

        when(incidentCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        IncidentCategory result = incidentCategoryService.findById(categoryId);

        assertEquals(category, result);
    }

    @Test
     void testFindById_CategoryNotFound() {
        Long categoryId = 1L;

        when(incidentCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentCategoryService.findById(categoryId));
    }

    @Test
     void testUpdate_CategoryExists() {
        IncidentCategoryDto updatedCategory = new IncidentCategoryDto(1L, "Existing Category");
        IncidentCategory existingCategory = new IncidentCategory(1L, "Existing Category");

        when(incidentCategoryRepository.findByName(updatedCategory.getName())).thenReturn(existingCategory);

        assertThrows(CustomException.class, () -> incidentCategoryService.update(updatedCategory));
    }


    @Test
    void testUpdate_CategoryNotFound() {
        IncidentCategoryDto updatedCategory = new IncidentCategoryDto(1L, "Updated Category");

        when(incidentCategoryRepository.findByName(updatedCategory.getName())).thenReturn(null);
        when(incidentCategoryRepository.findById(updatedCategory.getId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentCategoryService.update(updatedCategory));

        verify(incidentCategoryRepository, times(1)).findByName(updatedCategory.getName());
        verify(incidentCategoryRepository, times(1)).findById(updatedCategory.getId());
        verifyNoMoreInteractions(incidentCategoryRepository);
    }


    @Test
    void testUpdate_CategoryFound_ByIdNotFound() {
        IncidentCategoryDto updatedCategory = new IncidentCategoryDto(1L, "Updated Category");

        when(incidentCategoryRepository.findByName(updatedCategory.getName())).thenReturn(null);
        when(incidentCategoryRepository.findById(updatedCategory.getId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentCategoryService.update(updatedCategory));

        verify(incidentCategoryRepository, times(1)).findByName(updatedCategory.getName());
        verify(incidentCategoryRepository, times(1)).findById(updatedCategory.getId());
        verifyNoMoreInteractions(incidentCategoryRepository);
    }



    @Test
    void testUpdate_CategoryFound_ByIdFound() {
        IncidentCategoryDto updatedCategory = new IncidentCategoryDto(1L, "Updated Category");

        IncidentCategory existingCategory = new IncidentCategory();
        when(incidentCategoryRepository.findByName(updatedCategory.getName())).thenReturn(null);
        when(incidentCategoryRepository.findById(updatedCategory.getId())).thenReturn(Optional.of(existingCategory));
        when(incidentCategoryRepository.save(existingCategory)).thenReturn(existingCategory);

        IncidentCategory result = incidentCategoryService.update(updatedCategory);

        assertNotNull(result);
        assertEquals(existingCategory, result);

        verify(incidentCategoryRepository, times(1)).findByName(updatedCategory.getName());
        verify(incidentCategoryRepository, times(1)).findById(updatedCategory.getId());
        verify(incidentCategoryRepository, times(1)).save(existingCategory);
        verifyNoMoreInteractions(incidentCategoryRepository);
    }

    @Test
    void testDeleteIncidentCategories_AllFound() {
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(1L);
        categoryIds.add(2L);
        IncidentCategory category1 = new IncidentCategory(1L, "Category 1");
        IncidentCategory category2 = new IncidentCategory(2L, "Category 2");

        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(incidentCategoryRepository.findById(2L)).thenReturn(Optional.of(category2));

        incidentCategoryService.deleteIncidentCategories(categoryIds);

        verify(incidentCategoryRepository).deleteAllByIdIn(categoryIds);
    }

    @Test
    void testDeleteIncidentCategories_NotAllFound() {
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(1L);
        categoryIds.add(2L);

        when(incidentCategoryRepository.findById(1L)).thenReturn(Optional.of(new IncidentCategory(1L, "Category 1")));
        when(incidentCategoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> incidentCategoryService.deleteIncidentCategories(categoryIds));
    }


}