package com.trackswiftly.client_service.services;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.client_service.dao.repositories.PoiRepo;
import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.PoiResponse;
import com.trackswiftly.client_service.entities.Group;
import com.trackswiftly.client_service.entities.Poi;
import com.trackswiftly.client_service.entities.PoiType;
import com.trackswiftly.client_service.mappers.PoiMapper;
import com.trackswiftly.utils.base.services.TrackSwiftlyServiceAbstract;
import com.trackswiftly.utils.base.utils.TenantContext;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;
import com.trackswiftly.utils.exception.UnableToProccessIteamException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PoiService extends TrackSwiftlyServiceAbstract<Long , PoiRequest, PoiResponse> {

    private final PoiRepo poiRepo;

    private final PoiMapper poiMapper;


    @Autowired
    PoiService (
        PoiRepo poiRepo,
        PoiMapper poiMapper
    ) {
        this.poiRepo = poiRepo;
        this.poiMapper = poiMapper;
    }


    @Override
    public OperationResult deleteEntities(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return OperationResult.of(0);
        }

        int count = poiRepo.deleteByIds(ids); 

        return OperationResult.of(count) ;
    }


    @Override
    public List<PoiResponse> findEntities(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Poi> pois = poiRepo.findByIds(ids);

        return poiMapper.toPoiResponseList(pois);
    }


    @Override
    public PageDTO<PoiResponse> pageEntities(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive values");
        }


        List<PoiResponse> content = poiMapper.toPoiResponseList( // mappe Pois to Poisrespones
            poiRepo.findWithPagination(page, pageSize) // get grooup data
        ) ;


        long totalElements = poiRepo.count();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDTO<>(content, page, pageSize, totalElements, totalPages);
    }



    @Override
    protected List<PoiResponse> performCreateEntities(List<PoiRequest> poiRequests) {

        log.info("Creating poiRequests in batch...");
        

        List<Poi> pois = poiRepo.insertInBatch(poiMapper.toPoiList(poiRequests) ) ;

        log.debug("service : pois 📍: {}" , pois);

        List<PoiResponse> poiResponses = poiMapper.toPoiResponseList(pois);

        log.debug("service : poiResponses : {}" , poiResponses);

        return poiResponses ;
       
    }



    @Override
    protected OperationResult performUpdateEntities(List<Long> poiIds, PoiRequest poi) {
        if (poiIds == null || poiIds.isEmpty()) {
            throw new IllegalArgumentException("Poi IDs list cannot be null or empty");
        }

        if (poi == null) {
            throw new IllegalArgumentException("Poi object cannot be null");
        }

        int count = poiRepo.updateInBatch(poiIds, poiMapper.toPoi(poi)) ;

        return OperationResult.of(count);
    }




    /***
     * 
     * we use this method to validate the ownership and we call it before
     * we call {@link performCreateEntities} see {@link TrackSwiftlyService}.
     */
    @Override
    protected void validateCreate(List<PoiRequest> poiRequests) {

        // Extract unique type IDs and group IDs
        Set<Long> uniqueTypeIds = poiRequests.stream()
            .map(PoiRequest::getTypeId)
            .collect(Collectors.toSet());

        Set<Long> uniqueGroupIds = poiRequests.stream()
            .map(PoiRequest::getGroupId)
            .collect(Collectors.toSet());


        String tenantId = TenantContext.getTenantId() ;

        // Using CompletableFuture to run validations in parallel
        
        CompletableFuture<Long> typeCountFuture = CompletableFuture.supplyAsync(() -> {
            try {
                // Set tenant context for this thread
                TenantContext.setTenantId(tenantId);
                return poiRepo.countBasedOnIds(PoiType.class, uniqueTypeIds);
            } finally {
                TenantContext.clear(); // Cleanup to avoid context leaks
            }
        });
        
        CompletableFuture<Long> groupCountFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TenantContext.setTenantId(tenantId);
                return poiRepo.countBasedOnIds(Group.class, uniqueGroupIds);
            } finally {
                TenantContext.clear();
            }
        });

        // Wait for both validations to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(typeCountFuture, groupCountFuture);

        try {

            allFutures.join(); // Wait for both futures to complete
        
            // Get results from futures
            long truthTypeCount = typeCountFuture.get();
            long truthGroupCount = groupCountFuture.get();

            log.info("truthTypeCount : {}🧧 , uniqueTypeIds : {}" , truthTypeCount , uniqueTypeIds.size());
            log.info("truthGroupCount : {}🧧 , uniqueGroupIds : {}" , truthGroupCount , uniqueGroupIds.size());

            // Validate the results
            if (truthTypeCount != uniqueTypeIds.size() || truthGroupCount != uniqueGroupIds.size()) {
                throw new UnableToProccessIteamException("Access denied or unable to process the item");
            }
            
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new UnableToProccessIteamException("Access denied or unable to process the item , Validation Faild!") ;
        }
    }



    @Override
    protected void validateUpdate(List<Long> arg0, PoiRequest arg1) {
        throw new UnsupportedOperationException("Unimplemented method 'validateUpdate'");
    }
    
}
