package com.trackswiftly.vehicle_service.services;



import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.vehicle_service.dao.repositories.HomeLocationRepo;
import com.trackswiftly.vehicle_service.dtos.HomeLocationRequestDTO;
import com.trackswiftly.vehicle_service.dtos.HomeLocationResponseDTO;


import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.entities.HomeLocation;

import com.trackswiftly.vehicle_service.mappers.HomeLocationMapper;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class HomeLocationService {

    private HomeLocationMapper homeLocationMapper ;
    private HomeLocationRepo homeLocationRepo ;

    @Autowired
    HomeLocationService(
        HomeLocationMapper homeLocationMapper ,

        HomeLocationRepo homeLocationRepo
    ) {
        this.homeLocationMapper = homeLocationMapper ;

        this.homeLocationRepo = homeLocationRepo ;
    } 



    public List<HomeLocationResponseDTO> createHomeLocations(List<HomeLocationRequestDTO> homeLocationRequestDTOs) {
        

        List<HomeLocation> homeLocations = homeLocationRepo.insertInBatch(homeLocationMapper.toHomeLocationList(homeLocationRequestDTOs) ) ;


        return homeLocationMapper.toHomeLocationResponseDTOList(homeLocations);
    }


    public OperationResult deleteHomeLocations(List<Long> homeLocationIds) {

        if (homeLocationIds == null || homeLocationIds.isEmpty()) {
            return OperationResult.of(0);
        }

        int count = homeLocationRepo.deleteByIds(homeLocationIds); 

        return OperationResult.of(count) ;
    }


    public List<HomeLocationResponseDTO> findhomeLocations(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<HomeLocation> homeLocations = homeLocationRepo.findByIds(ids);

        return homeLocationMapper.toHomeLocationResponseDTOList(homeLocations);
    }


    public PageDTO<HomeLocationResponseDTO> gethomeLocationsWithPagination(int page, int pageSize) {

        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive values");
        }


        List<HomeLocationResponseDTO> content = homeLocationMapper.toHomeLocationResponseDTOList(
            homeLocationRepo.findWithPagination(page, pageSize) 
        ) ;


        long totalElements = homeLocationRepo.count();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDTO<>(content, page, pageSize, totalElements, totalPages);
    }



    public OperationResult updateHomeLocationsInBatch(List<Long> homeLocationIds, HomeLocationRequestDTO homeLocationRequestDTO) {
        
        if (homeLocationIds == null || homeLocationIds.isEmpty()) {
            throw new IllegalArgumentException("HomeLocation IDs list cannot be null or empty");
        }

        if (homeLocationRequestDTO == null) {
            throw new IllegalArgumentException("model object cannot be null");
        }

        int count = homeLocationRepo.updateInBatch(homeLocationIds, homeLocationMapper.toHomeLocation(homeLocationRequestDTO)) ;

        return OperationResult.of(count);
    }

}
