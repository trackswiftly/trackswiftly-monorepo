package com.trackswiftly.vehicle_service.services;



import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trackswiftly.vehicle_service.dao.repositories.VehicleRepo;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleResponse;
import com.trackswiftly.vehicle_service.entities.Vehicle;
import com.trackswiftly.vehicle_service.mappers.VehicleMapper;
import com.trackswiftly.vehicle_service.validators.EntityValidator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class VehicleService {

    private VehicleMapper vehicleMapper ;

    private VehicleRepo vehicleRepo ;

    private EntityValidator ownershipValidator ;


    VehicleService(
        VehicleMapper vehicleMapper ,
        VehicleRepo vehicleRepo ,
        EntityValidator ownershipValidator

    ) {
        this.vehicleMapper = vehicleMapper ;

        this.vehicleRepo = vehicleRepo ;

        this.ownershipValidator = ownershipValidator ;

    }


    public List<VehicleResponse> createVehicles(List<VehicleRequest> vehicleRequests) {
        
        ownershipValidator.validateVehicleRequest(vehicleRequests);

        List<Vehicle> vehicles = vehicleRepo.insertInBatch(vehicleMapper.toVehicleList(vehicleRequests) ) ;


        return vehicleMapper.toVehicleResponseList(vehicles);
    }


    public OperationResult deleteVehicles(List<Long> vehicleIds) {

        if (vehicleIds == null || vehicleIds.isEmpty()) {
            return OperationResult.of(0);
        }

        int count = vehicleRepo.deleteByIds(vehicleIds); 

        return OperationResult.of(count) ;
    }


    public List<VehicleResponse> findVehicles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Vehicle> vehicles = vehicleRepo.findByIds(ids);

        return vehicleMapper.toVehicleResponseList(vehicles);
    }



    public PageDTO<VehicleResponse> getVehiclesWithPagination(int page, int pageSize) {

        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive values");
        }


        List<VehicleResponse> content = vehicleMapper.toVehicleResponseList(
            vehicleRepo.findWithPagination(page, pageSize)
        ) ;


        long totalElements = vehicleRepo.count();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDTO<>(content, page, pageSize, totalElements, totalPages);
    }




    public OperationResult updateVehiclesInBatch(List<Long> vehicleIds, VehicleRequest vehicle) {
        
        if (vehicleIds == null || vehicleIds.isEmpty()) {
            throw new IllegalArgumentException("Vehicle IDs list cannot be null or empty");
        }

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle object cannot be null");
        }

        /*
         * 
         * OWENERSHIP VALIDATION
         */

         ownershipValidator.validateVehicleRequest(List.of(vehicle));

        int count = vehicleRepo.updateInBatch(vehicleIds, vehicleMapper.toVehicle(vehicle)) ;

        return OperationResult.of(count);
    }
    
}
