package com.trackswiftly.vehicle_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;


import com.trackswiftly.vehicle_service.dtos.HomeLocationRequestDTO;
import com.trackswiftly.vehicle_service.dtos.HomeLocationResponseDTO;
import com.trackswiftly.vehicle_service.entities.HomeLocation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HomeLocationMapper {
    


    public List<HomeLocationResponseDTO> toHomeLocationResponseDTOList(List<HomeLocation> homeLocations) {


        return homeLocations.stream()
                            .map(this::toHomeLocationResponseDTO)
                            .toList() ;
    }


    public List<HomeLocation> toHomeLocationList(List<HomeLocationRequestDTO> homeLocationRequests) {
        return homeLocationRequests.stream()
                .map(this::toHomeLocation)
                .toList();
    }



    public HomeLocationResponseDTO toHomeLocationResponseDTO(HomeLocation homeLocation) {
        return HomeLocationResponseDTO.builder()
                .id(homeLocation.getId())
                .name(homeLocation.getName())
                .latitude(homeLocation.getLatitude())
                .longitude(homeLocation.getLongitude())
                .updatedAt(homeLocation.getUpdatedAt())
                .createdAt(homeLocation.getCreatedAt())
                .build();
    }
    

    public HomeLocation toHomeLocation(HomeLocationRequestDTO homeLocationRequest) {
        return HomeLocation.builder()
                .name(homeLocationRequest.getName())
                .latitude(homeLocationRequest.getLatitude())
                .longitude(homeLocationRequest.getLongitude())
                .build();
    }
}
