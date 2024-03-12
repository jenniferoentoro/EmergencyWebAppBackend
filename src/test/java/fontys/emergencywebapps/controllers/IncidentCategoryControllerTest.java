//package fontys.emergencywebapps.controllers;
//
//import fontys.emergencywebapps.business.IncidentCategoryUseCases;
//import fontys.emergencywebapps.controllers.dto.IncidentCategoryDto;
//import fontys.emergencywebapps.controllers.dto.IncidentCategoryRequest;
//import fontys.emergencywebapps.persistence.entities.IncidentCategory;
//import lombok.With;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//class IncidentCategoryControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private IncidentCategoryUseCases incidentCategoryUseCases;
//
//    @Test
//    @WithMockUser(username = "c14200152")
//    void getIncidentCategory_shouldReturn200WithIncidentCategory_whenIncidentCategoryFound() throws Exception {
//        IncidentCategory incidentCategory = IncidentCategory.builder()
//                .id(1L)
//                .name("testIncidentCategory")
//                .build();
//
//        when(incidentCategoryUseCases.findById(1L)).thenReturn(incidentCategory);
//
//        mockMvc.perform(get("/api/incident-categories/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
//                .andExpect(content().json("""
//                        {"id":1,"name":"testIncidentCategory"}"""));
//
//        verify(incidentCategoryUseCases).findById(1L);
//    }
//
//    @Test
//    @WithMockUser(username = "jenniferoentoro", roles = {"ADMIN"})
//    void getAllIncidentCategories_shouldReturn200WithCategoriesList_WhenNoFilterProvided() throws Exception {
//
//        List<IncidentCategory> response = List.of(
//                IncidentCategory.builder().id(1L).name("testIncidentCategory1").build(),
//                IncidentCategory.builder().id(2L).name("testIncidentCategory2").build()
//        );
//
//        when(incidentCategoryUseCases.findAll()).thenReturn(response);
//
//        mockMvc.perform(get("/api/incident-categories"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
//                .andExpect(content().json("""
//                        [{"id":1,"name":"testIncidentCategory1"},{"id":2,"name":"testIncidentCategory2"}]"""));
//
//        verify(incidentCategoryUseCases).findAll();
//
//    }
//
//
//    @Test
//    @WithMockUser(username = "jenniferoentoro", roles = {"ADMIN"})
//    void deleteIncidentCategory_shouldReturn204() throws Exception {
//        mockMvc.perform(delete("/api/incident-categories/1"))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//
//        verify(incidentCategoryUseCases).deleteById(1L);
//    }
//
//    @Test
//    @WithMockUser(username = "jenniferoentoro", roles = {"ADMIN"})
//    void deleteIncidentCategories_shouldReturn204() throws Exception {
//        mockMvc.perform(delete("/api/incident-categories")
//                .contentType(APPLICATION_JSON_VALUE)
//                .content("""
//                        {"ids":[1,2,3]}"""))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//
//        verify(incidentCategoryUseCases).deleteIncidentCategories(List.of(1L, 2L, 3L));
//    }
//
//    @Test
//    @WithMockUser(username = "jenniferoentoro", roles = {"ADMIN"})
//    void createIncidentCategory_shouldReturn200WithIncidentCategory() throws Exception {
//        IncidentCategoryRequest incidentCategory = IncidentCategoryRequest.builder()
//                .name("testIncidentCategory")
//                .build();
//        IncidentCategory savedIncidentCategory = IncidentCategory.builder()
//                .id(1L)
//                .name("testIncidentCategory")
//                .build();
//        when(incidentCategoryUseCases.save(incidentCategory)).thenReturn(savedIncidentCategory);
//        ResultActions resultActions = mockMvc.perform(post("/api/incident-categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"testIncidentCategory\"}"))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        String expectedJson = "{\"id\":1,\"name\":\"testIncidentCategory\"}";
//        resultActions.andExpect(content().json(expectedJson, true));
//        verify(incidentCategoryUseCases).save(incidentCategory);
//    }
//
//    @Test
//    @WithMockUser(username = "jenniferoentoro", roles = {"ADMIN"})
//    void updateIncidentCategory_shouldReturn200WithIncidentCategory() throws Exception {
//        IncidentCategoryDto incidentCategory = IncidentCategoryDto.builder()
//                .id(1L)
//                .name("testIncidentCategory")
//                .build();
//        IncidentCategory updatedIncidentCategory = IncidentCategory.builder()
//                .id(1L)
//                .name("testIncidentCategory")
//                .build();
//        when(incidentCategoryUseCases.update(incidentCategory)).thenReturn(updatedIncidentCategory);
//        ResultActions resultActions = mockMvc.perform(put("/api/incident-categories/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":1,\"name\":\"testIncidentCategory\"}"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        String expectedJson = "{\"id\":1,\"name\":\"testIncidentCategory\"}";
//        resultActions.andExpect(content().json(expectedJson, true));
//        verify(incidentCategoryUseCases).update(incidentCategory);
//    }
//
//}