package com.trackswiftly.client_service.tests.services;



import com.trackswiftly.utils.base.utils.TenantContext;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;
import com.trackswiftly.client_service.dao.repositories.PoiRepo;
import com.trackswiftly.client_service.entities.Group;
import com.trackswiftly.client_service.entities.Poi;
import com.trackswiftly.client_service.entities.PoiType;
import com.trackswiftly.client_service.mappers.PoiMapper;
import com.trackswiftly.client_service.services.PoiService;
import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.PoiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisabledInNativeImage
class PoiServiceTest {


    @Mock
    private PoiRepo poiRepo;

    @Mock
    private PoiMapper poiMapper;

    @InjectMocks
    private PoiService poiService;

    private PoiRequest poiRequest;
    private Poi poi;
    private PoiResponse poiResponse;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        // Set a tenant id for the test
        TenantContext.setTenantId("tenant1");

        // Create a sample request, entity, and response.
        poiRequest = new PoiRequest();
        poiRequest.setTypeId(1L);
        poiRequest.setGroupId(2L);

        poi = new Poi();
        // Populate fields on poi as necessary

        poiResponse = PoiResponse.builder()
                                    .build();
        // Populate fields on poiResponse as necessary

        // Default behavior for the mapper in createEntities:
        when(poiMapper.toPoiList(any())).thenReturn(Collections.singletonList(poi));
        when(poiMapper.toPoiResponseList(any())).thenReturn(Collections.singletonList(poiResponse));

        // For validateCreate, simulate that the counts match the unique IDs.
        // For a single request, both unique sets have size 1.
        when(poiRepo.countBasedOnIds(eq(PoiType.class), any(Set.class))).thenReturn(1L);
        when(poiRepo.countBasedOnIds(eq(Group.class), any(Set.class))).thenReturn(1L);
    }



    @Test
    void testCreateEntities_validRequests() {
        // Given a list with one request
        List<PoiRequest> requests = Collections.singletonList(poiRequest);
        // Simulate insertInBatch call
        when(poiRepo.insertInBatch(any())).thenReturn(Collections.singletonList(poi));

        // When createEntities is called, it should first call validateCreate
        // (which internally calls countBasedOnIds) and then performCreateEntities.
        List<PoiResponse> responses = poiService.createEntities(requests);

        // Verify that the validations were performed by checking countBasedOnIds calls.
        verify(poiRepo, times(1)).countBasedOnIds(eq(PoiType.class), any(Set.class));
        verify(poiRepo, times(1)).countBasedOnIds(eq(Group.class), any(Set.class));

        // Verify that performCreateEntities is executed (insertInBatch is invoked).
        verify(poiRepo, times(1)).insertInBatch(any());
       

        // Assert the response is as expected.
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }


    @Test
    void testUpdateEntities_shouldThrowUnsupportedOperationException() {
        // updateEntities in PoiService calls validateUpdate, which is unimplemented and should throw an exception.
        List<Long> ids = Collections.singletonList(1L);
        assertThrows(UnsupportedOperationException.class, () -> {
            poiService.updateEntities(ids, poiRequest);
        });
    }


    @Test
    void testDeleteEntities_validIds() {
        List<Long> ids = Collections.singletonList(1L);
        // Simulate deleteByIds returning count 1
        when(poiRepo.deleteByIds(ids)).thenReturn(1);
        OperationResult result = poiService.deleteEntities(ids);
        assertNotNull(result);
    }


    @Test
    void testFindEntities_validIds() {
        List<Long> ids = Collections.singletonList(1L);
        when(poiRepo.findByIds(ids)).thenReturn(Collections.singletonList(poi));
        when(poiMapper.toPoiResponseList(Collections.singletonList(poi))).thenReturn(Collections.singletonList(poiResponse));
        List<PoiResponse> responses = poiService.findEntities(ids);
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }


    @Test
    void testPageEntities_validInput() {
        int page = 0;
        int pageSize = 10;
        when(poiRepo.findWithPagination(page, pageSize)).thenReturn(Collections.singletonList(poi));
        when(poiRepo.count()).thenReturn(1L);
        when(poiMapper.toPoiResponseList(any())).thenReturn(Collections.singletonList(poiResponse));

        PageDTO<PoiResponse> pageDTO = poiService.pageEntities(page, pageSize);
        assertNotNull(pageDTO);
        assertEquals(1, pageDTO.getContent().size());
        assertEquals(1L, pageDTO.getTotalElements());
        assertEquals(1, pageDTO.getTotalPages());
    }


    
    
}
