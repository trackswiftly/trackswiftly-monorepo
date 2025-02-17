package com.trackswiftly.vehicle_service.services;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trackswiftly.vehicle_service.dao.repositories.GroupRepo;
import com.trackswiftly.vehicle_service.dao.repositories.ModelRepo;
import com.trackswiftly.vehicle_service.dao.repositories.VehicleRepo;
import com.trackswiftly.vehicle_service.dao.repositories.VehicleTypeRepo;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleResponse;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.entities.Vehicle;
import com.trackswiftly.vehicle_service.entities.VehicleType;
import com.trackswiftly.vehicle_service.exception.UnableToProccessIteamException;
import com.trackswiftly.vehicle_service.mappers.VehicleMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class VehicleService {

    private VehicleMapper vehicleMapper ;

    private VehicleRepo vehicleRepo ;

    private GroupRepo groupRepo ;

    private VehicleTypeRepo vehicleTypeRepo ;

    private ModelRepo modelRepo ;



    VehicleService(
        VehicleMapper vehicleMapper ,
        VehicleRepo vehicleRepo ,
        GroupRepo groupRepo ,

        VehicleTypeRepo vehicleTypeRepo ,

        ModelRepo modelRepo

    ) {
        this.vehicleMapper = vehicleMapper ;

        this.vehicleRepo = vehicleRepo ;

        this.groupRepo = groupRepo ;

        this.vehicleTypeRepo = vehicleTypeRepo ;

        this.modelRepo = modelRepo ;
    }


    public List<VehicleResponse> createVehicles(List<VehicleRequest> vehicleRequests) {
        
        
        validateVehicleRequests(vehicleRequests);

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

        int count = vehicleRepo.updateInBatch(vehicleIds, vehicleMapper.toVehicle(vehicle)) ;

        return OperationResult.of(count);
    }
    




    

    private void validateVehicleRequests(List<VehicleRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return;
        }

        // Extract unique IDs
        Set<Long> vehicleTypeIds = requests.stream()
            .map(VehicleRequest::getVehicleTypeId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        Set<Long> modelIds = requests.stream()
            .map(VehicleRequest::getModelId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        Set<Long> groupIds = requests.stream()
            .map(VehicleRequest::getVehicleGroupId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        // Fetch entities
        Map<Long, VehicleType> vehicleTypesMap = vehicleTypeRepo.findByIds(new ArrayList<>(vehicleTypeIds))
            .stream()
            .collect(Collectors.toMap(VehicleType::getId, Function.identity()));

        Map<Long, Model> modelsMap = modelRepo.findByIds(new ArrayList<>(modelIds))
            .stream()
            .collect(Collectors.toMap(Model::getId, Function.identity()));

        Map<Long, Group> groupsMap = groupRepo.findByIds(new ArrayList<>(groupIds))
            .stream()
            .collect(Collectors.toMap(Group::getId, Function.identity()));

        // Validate entities
        List<String> errors = new ArrayList<>();
        
        for (VehicleRequest request : requests) {
            if (!vehicleTypesMap.containsKey(request.getVehicleTypeId())) {
                errors.add("VehicleType with ID " + request.getVehicleTypeId() + 
                    " not found or does not belong to the tenant");
            }
            if (!modelsMap.containsKey(request.getModelId())) {
                errors.add("Model with ID " + request.getModelId() + 
                    " not found or does not belong to the tenant");
            }
            if (!groupsMap.containsKey(request.getVehicleGroupId())) {
                errors.add("Group with ID " + request.getVehicleGroupId() + 
                    " not found or does not belong to the tenant");
            }
        }

        if (!errors.isEmpty()) {
            throw new UnableToProccessIteamException(String.join("; ", errors));
        }
    }



    
}
