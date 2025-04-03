package com.trackswiftly.vehicle_service.tests.web;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.http.MediaType.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.PoiResponse;
import com.trackswiftly.client_service.services.PoiService;
import com.trackswiftly.client_service.web.PoiController;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;

@ExtendWith(MockitoExtension.class)
class PoiControllerTest {


    @InjectMocks
    private PoiController poiController;

    @Mock
    private PoiService poiService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(poiController).build();
    }



    @Test
    void testCreatePois() throws Exception {
        List<PoiRequest> poiRequests = Arrays.asList(
                PoiRequest.builder().name("POI 1").build(),
                PoiRequest.builder().name("POI 2").build()
        );
        List<PoiResponse> poiResponses = Arrays.asList(
                PoiResponse.builder().id(1L).name("POI 1").build(),
                PoiResponse.builder().id(2L).name("POI 2").build()
        );

        when(poiService.createEntities(poiRequests)).thenReturn(poiResponses);

        mockMvc.perform(post("/pois")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(poiRequests)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("POI 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("POI 2"));

        verify(poiService, times(1)).createEntities(poiRequests);
    }



    @Test
    void testDeletePois() throws Exception {
        List<Long> poiIds = Arrays.asList(1L, 2L);
        OperationResult result = OperationResult.of(2);

        when(poiService.deleteEntities(poiIds)).thenReturn(result);

        mockMvc.perform(delete("/pois/1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.affectedRecords").value(2));

        verify(poiService, times(1)).deleteEntities(poiIds);
    }



    @Test
    void testFindPois() throws Exception {
        List<Long> poiIds = Arrays.asList(1L, 2L);
        List<PoiResponse> poiResponses = Arrays.asList(
                PoiResponse.builder().id(1L).name("POI 1").build(),
                PoiResponse.builder().id(2L).name("POI 2").build()
        );

        when(poiService.findEntities(poiIds)).thenReturn(poiResponses);

        mockMvc.perform(get("/pois/1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("POI 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("POI 2"));

        verify(poiService, times(1)).findEntities(poiIds);
    }



    @Test
    void testGetPoisWithPagination() throws Exception {
        PageDTO<PoiResponse> pageDTO = new PageDTO<>(Arrays.asList(
            PoiResponse.builder().id(1L).name("POI 1").build()
        ), 0, 1, 10L, 10);

        when(poiService.pageEntities(0, 1)).thenReturn(pageDTO);

        mockMvc.perform(get("/pois?page=0&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(10))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("POI 1"));

        verify(poiService, times(1)).pageEntities(0, 1);
    }




    @Test
    void testUpdatePoisInBatch() throws Exception {
        List<Long> poiIds = Arrays.asList(1L, 2L);
        PoiRequest poiRequest = PoiRequest.builder()
                .name("Updated POI")
                .build();
        OperationResult result = OperationResult.of(2);

        when(poiService.updateEntities(poiIds, poiRequest)).thenReturn(result);

        mockMvc.perform(put("/pois/1,2")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(poiRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.affectedRecords").value(2));

        verify(poiService, times(1)).updateEntities(poiIds, poiRequest);
    }


    
}
