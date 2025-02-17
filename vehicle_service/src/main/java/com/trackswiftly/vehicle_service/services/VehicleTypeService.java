package com.trackswiftly.vehicle_service.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.vehicle_service.annotations.LogUserOperation;
import com.trackswiftly.vehicle_service.dao.repositories.VehicleTypeRepo;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.VehicleTypeRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleTypeResponse;
import com.trackswiftly.vehicle_service.mappers.VehicleTypeMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
@Transactional
public class VehicleTypeService {

    private VehicleTypeRepo vehicleTypeRepo ;
    private VehicleTypeMapper vehicleTypeMapper ;


    @Autowired
    VehicleTypeService(
        VehicleTypeRepo vehicleTypeRepo ,
        VehicleTypeMapper vehicleTypeMapper
    ) {
        this.vehicleTypeRepo = vehicleTypeRepo ;

        this.vehicleTypeMapper = vehicleTypeMapper ;
    }



    public List<VehicleTypeResponse> createVehicleTypes(List<VehicleTypeRequest> vehicleTypeRequests) {

        log.info("Creating groups in batch...");

        return vehicleTypeMapper.toVehicleTypeResponseList(
            vehicleTypeRepo.insertInBatch(
                vehicleTypeMapper.toVehicleTypeList(
                    vehicleTypeRequests
                ) 
            )
        ) ;
    }


    public OperationResult deleteVehicleTypes(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return OperationResult.of(0);
        }

        int count = vehicleTypeRepo.deleteByIds(ids); 

        return OperationResult.of(count) ;
    }


    public List<VehicleTypeResponse> findVehicleTypes(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return vehicleTypeMapper.toVehicleTypeResponseList(
            vehicleTypeRepo.findByIds(ids)
        );
    }



    public PageDTO<VehicleTypeResponse> getVehicleTypesWithPagination(int page, int pageSize) {

        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive values");
        }


        List<VehicleTypeResponse> content = vehicleTypeMapper.toVehicleTypeResponseList( // mappe groups to groupsrespones
            vehicleTypeRepo.findWithPagination(page, pageSize) // get grooup data
        ) ;


        long totalElements = vehicleTypeRepo.count();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDTO<>(content, page, pageSize, totalElements, totalPages);
    }



    @LogUserOperation("Update VehicleTypes in batch")
    public OperationResult updateVehicleTypesInBatch(List<Long> ids, VehicleTypeRequest vehicleType) {
        
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("VehicleType IDs list cannot be null or empty");
        }

        if (vehicleType == null) {
            throw new IllegalArgumentException("VehicleType object cannot be null");
        }

        int count = vehicleTypeRepo.updateInBatch(ids, vehicleTypeMapper.toVehicleType(vehicleType)) ;

        return OperationResult.of(count);
    }
    
}
