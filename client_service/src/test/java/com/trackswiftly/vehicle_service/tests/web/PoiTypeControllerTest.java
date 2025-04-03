package com.trackswiftly.vehicle_service.tests.web;



import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackswiftly.client_service.dtos.PoiTypeRequest;
import com.trackswiftly.client_service.dtos.PoiTypeResponse;
import com.trackswiftly.client_service.services.PoiTypeService;
import com.trackswiftly.client_service.web.PoiTypeController;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@DisabledInNativeImage
@ExtendWith(MockitoExtension.class)
class PoiTypeControllerTest {



    @InjectMocks
    private PoiTypeController poiTypeController;

    @Mock
    private PoiTypeService poiTypeService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(poiTypeController).build();
    }




    @Test
    void testCreatePoiTypes() throws Exception {
        List<PoiTypeRequest> poiTypeRequests = Arrays.asList(
                PoiTypeRequest.builder().name("Type 1").build(),
                PoiTypeRequest.builder().name("Type 2").build()
        );
        List<PoiTypeResponse> poiTypeResponses = Arrays.asList(
                PoiTypeResponse.builder().id(1L).name("Type 1").build(),
                PoiTypeResponse.builder().id(2L).name("Type 2").build()
        );

        when(poiTypeService.createEntities(poiTypeRequests)).thenReturn(poiTypeResponses);

        mockMvc.perform(post("/types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(poiTypeRequests)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Type 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Type 2"));

        verify(poiTypeService, times(1)).createEntities(poiTypeRequests);
    }
    




    @Test
    void testDeletePoiTypes() throws Exception {
        List<Long> typeIds = Arrays.asList(1L, 2L);
        OperationResult result = OperationResult.of(2);

        when(poiTypeService.deleteEntities(typeIds)).thenReturn(result);

        mockMvc.perform(delete("/types/1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.affectedRecords").value(2));

        verify(poiTypeService, times(1)).deleteEntities(typeIds);
    }





    @Test
    void testFindPoiTypes() throws Exception {
        List<Long> typeIds = Arrays.asList(1L, 2L);
        List<PoiTypeResponse> poiTypeResponses = Arrays.asList(
                PoiTypeResponse.builder().id(1L).name("Type 1").build(),
                PoiTypeResponse.builder().id(2L).name("Type 2").build()
        );

        when(poiTypeService.findEntities(typeIds)).thenReturn(poiTypeResponses);

        mockMvc.perform(get("/types/1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Type 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Type 2"));

        verify(poiTypeService, times(1)).findEntities(typeIds);
    }







    @Test
    void testGetPoiTypesWithPagination() throws Exception {
        PageDTO<PoiTypeResponse> pageDTO = new PageDTO<>(Arrays.asList(
                PoiTypeResponse.builder().id(1L).name("Type 1").build()
        ), 0, 1, 10L, 10);

        when(poiTypeService.pageEntities(0, 1)).thenReturn(pageDTO);

        mockMvc.perform(get("/types?page=0&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(10))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Type 1"));

        verify(poiTypeService, times(1)).pageEntities(0, 1);
    }




    @Test
    void testUpdatePoiTypesInBatch() throws Exception {
        List<Long> typeIds = Arrays.asList(1L, 2L);
        PoiTypeRequest poiTypeRequest = PoiTypeRequest.builder()
                .name("Updated Type")
                .build();
        OperationResult result = OperationResult.of(2);

        when(poiTypeService.updateEntities(typeIds, poiTypeRequest)).thenReturn(result);

        mockMvc.perform(put("/types/1,2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(poiTypeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.affectedRecords").value(2));

        verify(poiTypeService, times(1)).updateEntities(typeIds, poiTypeRequest);
    }
}
