package com.trackswiftly.client_service.tests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trackswiftly.client_service.dao.repositories.PoiTypeRepo;
import com.trackswiftly.client_service.dtos.PoiTypeRequest;
import com.trackswiftly.client_service.dtos.PoiTypeResponse;
import com.trackswiftly.client_service.entities.Poi;
import com.trackswiftly.client_service.entities.PoiType;
import com.trackswiftly.client_service.mappers.PoiTypeMapper;
import com.trackswiftly.client_service.services.PoiTypeService;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;



@DisabledInNativeImage
@ExtendWith(MockitoExtension.class)
class PoiTypeServiceTest {


    @InjectMocks
    private PoiTypeService poiTypeService;

    @Mock
    private PoiTypeRepo poiTypeRepo;

    @Mock
    private PoiTypeMapper poiTypeMapper;

    private List<PoiType> poiTypes;
    private List<PoiTypeRequest> poiTypeRequests;
    private List<PoiTypeResponse> poiTypeResponses;



    @BeforeEach
    void setUp() {
        poiTypeRequests = Arrays.asList(
            PoiTypeRequest.builder()
                .name("Type1")
                .build(),
            PoiTypeRequest.builder()
                .name("Type2")
                .build()
        );

        poiTypes = Arrays.asList(
            PoiType.builder()
                .id(1L)
                .name("Type1")
                .build(),
            PoiType.builder()
                .id(2L)
                .name("Type2")
                .build()
        );

        poiTypeResponses = Arrays.asList(
            PoiTypeResponse.builder()
                .id(1L)
                .name("Type1")
                .build(),
            PoiTypeResponse.builder()
                .id(2L)
                .name("Type2")
                .build()
        );
    }


    @Test
    void testCreateEntities() {
        when(poiTypeMapper.toPoiTypeList(poiTypeRequests)).thenReturn(poiTypes);
        when(poiTypeRepo.insertInBatch(poiTypes)).thenReturn(poiTypes);
        when(poiTypeMapper.toPoiTypeResponseList(poiTypes)).thenReturn(poiTypeResponses);

        List<PoiTypeResponse> result = poiTypeService.createEntities(poiTypeRequests);

        assertEquals(poiTypeResponses, result);
        verify(poiTypeRepo, times(1)).insertInBatch(poiTypes);
    }


    @Test
    void testDeleteEntities() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(poiTypeRepo.deleteByIds(ids)).thenReturn(2);

        OperationResult result = poiTypeService.deleteEntities(ids);

        assertEquals(2, result.affectedRecords());
        verify(poiTypeRepo, times(1)).deleteByIds(ids);
    }


    @Test
    void testFindEntities() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(poiTypeRepo.findByIds(ids)).thenReturn(poiTypes);
        when(poiTypeMapper.toPoiTypeResponseList(poiTypes)).thenReturn(poiTypeResponses);

        List<PoiTypeResponse> result = poiTypeService.findEntities(ids);

        assertEquals(poiTypeResponses, result);
        verify(poiTypeRepo, times(1)).findByIds(ids);
    }



    @Test
    void testPageEntities() {
        int page = 0, pageSize = 2;
        when(poiTypeRepo.findWithPagination(page, pageSize)).thenReturn(poiTypes);
        when(poiTypeMapper.toPoiTypeResponseList(poiTypes)).thenReturn(poiTypeResponses);
        when(poiTypeRepo.count()).thenReturn(10L);

        PageDTO<PoiTypeResponse> result = poiTypeService.pageEntities(page, pageSize);

        assertEquals(10L, result.getTotalElements());
        assertEquals(5, result.getTotalPages());
        verify(poiTypeRepo, times(1)).findWithPagination(page, pageSize);
    }



    @Test
    void testUpdateEntities() {
        List<Long> ids = Arrays.asList(1L, 2L);
        PoiTypeRequest request = PoiTypeRequest.builder()
                                                .name("Updated Type")
                                                .build();
        PoiType updatedPoiType = PoiType.builder()
                                        .name("Updated Type")
                                        .build();
        when(poiTypeMapper.toPoiType(request)).thenReturn(updatedPoiType);
        when(poiTypeRepo.updateInBatch(ids, updatedPoiType)).thenReturn(2);

        OperationResult result = poiTypeService.updateEntities(ids, request);

        assertEquals(2, result.affectedRecords());
        verify(poiTypeRepo, times(1)).updateInBatch(ids, updatedPoiType);
    }
    
}
