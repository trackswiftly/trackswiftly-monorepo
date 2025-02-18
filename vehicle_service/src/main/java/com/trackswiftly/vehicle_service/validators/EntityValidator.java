package com.trackswiftly.vehicle_service.validators;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.exception.UnableToProccessIteamException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityValidator {


    @PersistenceContext
    private EntityManager em;



     /**
     * Validates ownership of entities specified in the request
     */
    public void validateVehicleRequest(List<VehicleRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return;
        }

        // Extract all IDs
        List<Long> vehicleTypeIds = extractIds(requests, VehicleRequest::getVehicleTypeId);
        List<Long> modelIds = extractIds(requests, VehicleRequest::getModelId);
        List<Long> groupIds = extractIds(requests, VehicleRequest::getVehicleGroupId);

        // Validate each entity type
        validateEntities(vehicleTypeIds, "VehicleType", "One or more vehicle types do not exist or do not belong to the current tenant");
        validateEntities(modelIds, "Model", "One or more models do not exist or do not belong to the current tenant");
        validateEntities(groupIds, "Group", "One or more groups do not exist or do not belong to the current tenant");
    }


    /**
     * Generic method to extract IDs from a list of objects
     */
    private <T, R> List<R> extractIds(Collection<T> items, Function<T, R> getter) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        
        return items.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }



    /**
     * Validates that all entities exist and belong to current tenant
     */
    private void validateEntities(List<Long> ids, String entityName, String errorMessage) {
        if (ids.isEmpty()) {
            return;
        }

        String hql = String.format(
            "SELECT COUNT(e.id) FROM %s e WHERE e.id IN :ids",
            entityName
        );
        
        Query query = em.createQuery(hql);
        query.setParameter("ids", ids);
        long count = (long) query.getSingleResult();
        
        em.clear();
        
        if (count != ids.size()) {
            throw new UnableToProccessIteamException(errorMessage);
        }
    }
    
}
