package com.trackswiftly.client_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.client_service.dtos.PoiTypeRequest;
import com.trackswiftly.client_service.dtos.PoiTypeResponse;
import com.trackswiftly.client_service.entities.PoiType;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class PoiTypeMapper {



    public List<PoiTypeResponse> toPoiTypeResponseList(List<PoiType> poiTypes) {


        List<PoiTypeResponse> poiTypeResponses = poiTypes.stream()
                                            .map(this::toPoiTypeResponse)
                                            .toList() ;
        
        log.debug("PoiTypeResponses :{} " , poiTypeResponses);
        return poiTypeResponses;
    }


    public List<PoiType> toPoiTypeList(List<PoiTypeRequest> poiTypeRequests) {
        return poiTypeRequests.stream()
                .map(this::toPoiType)
                .toList();
    }
    



    public PoiTypeResponse toPoiTypeResponse(PoiType poiType) {
        return PoiTypeResponse.builder()
                .id(poiType.getId())
                .name(poiType.getName())
                .description(poiType.getDescription())
                .updatedAt(poiType.getUpdatedAt())
                .createdAt(poiType.getCreatedAt())
                .build();
    }
    

    public PoiType toPoiType(PoiTypeRequest poiTypeRequest) {
        return PoiType.builder()
                .name(poiTypeRequest.getName())
                .description(poiTypeRequest.getDescription())
                .build();
    }
}
