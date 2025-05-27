package com.trackswiftly.client_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.client_service.dtos.GroupResponse;
import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.PoiResponse;
import com.trackswiftly.client_service.dtos.PoiTypeResponse;
import com.trackswiftly.client_service.entities.Group;
import com.trackswiftly.client_service.entities.Poi;
import com.trackswiftly.client_service.entities.PoiType;
import com.trackswiftly.utils.enums.Capacity;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class PoiMapper {



    public List<PoiResponse> toPoiResponseList(List<Poi> pois) {


        List<PoiResponse> poiResponses = pois.stream()
                                            .map(this::toPoiResponse)
                                            .toList() ;
        
        log.debug("PoiResponses :{} " , poiResponses);
        return poiResponses;
    }


    public List<Poi> toPoiList(List<PoiRequest> poiRequests) {
        return poiRequests.stream()
                .map(this::toPoi)
                .toList();
    }
    



    public PoiResponse toPoiResponse(Poi poi) {
        return PoiResponse.builder()
                .id(poi.getId())
                .name(poi.getName())
                .group(
                        GroupResponse.builder()
                                .id(poi.getGroup().getId())
                                .name(poi.getGroup().getName())
                                .build()
                )
                .poiTypeResponse(
                        PoiTypeResponse.builder()
                                .id(poi.getType().getId())
                                .name(poi.getType().getName())
                                .build()
                )
                .defaultCapacityType(poi.getDefaultCapacityType())
                .capacity(poi.getCapacity())
                .address(poi.getAddress())
                .latitude(poi.getLatitude())
                .longitude(poi.getLongitude())
                .payload(poi.getPayload())
                .updatedAt(poi.getUpdatedAt())
                .createdAt(poi.getCreatedAt())
                .build();
    }
    

    public Poi toPoi(PoiRequest poiRequest) {

        log.debug("payload 📦 :{} " , poiRequest.getPayload());
        return Poi.builder()
                .name(poiRequest.getName())
                .group(
                        Group.builder()
                                .id(poiRequest.getGroupId())
                                .build()
                )
                .type(
                        PoiType.builder()
                                .id(poiRequest.getTypeId())
                                .build()
                )
                .defaultCapacityType(poiRequest.getDefaultCapacityType())
                .capacity(
                        Capacity.builder()
                                .maxWeightKg(poiRequest.getCapacity().getMaxWeightKg())
                                .maxBoxes(poiRequest.getCapacity().getMaxBoxes())
                                .maxVolumeM3(poiRequest.getCapacity().getMaxVolumeM3())
                                .maxPallets(poiRequest.getCapacity().getMaxPallets())
                                .build()
                )
                .address(poiRequest.getAddress())
                .latitude(poiRequest.getLatitude())
                .longitude(poiRequest.getLongitude())
                .payload(poiRequest.getPayload())
                .build();
    }
}
