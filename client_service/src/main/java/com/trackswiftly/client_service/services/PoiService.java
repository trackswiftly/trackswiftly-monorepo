package com.trackswiftly.client_service.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.client_service.dao.repositories.PoiRepo;
import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.PoiResponse;
import com.trackswiftly.client_service.entities.Poi;
import com.trackswiftly.client_service.mappers.PoiMapper;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;
import com.trackswiftly.utils.interfaces.TrackSwiftlyService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PoiService implements TrackSwiftlyService<Long , PoiRequest , PoiResponse> {

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
    public List<PoiResponse> createEntities(List<PoiRequest> poiRequests) {
        log.info("Creating poiRequests in batch...");
        

        List<Poi> pois = poiRepo.insertInBatch(poiMapper.toPoiList(poiRequests) ) ;

        log.debug("service : pois 📍: {}" , pois);

        List<PoiResponse> poiResponses = poiMapper.toPoiResponseList(pois);

        log.debug("service : poiResponses : {}" , poiResponses);

        return poiResponses ;
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
    public OperationResult updateEntities(List<Long> poiIds, PoiRequest poi) {

        if (poiIds == null || poiIds.isEmpty()) {
            throw new IllegalArgumentException("Poi IDs list cannot be null or empty");
        }

        if (poi == null) {
            throw new IllegalArgumentException("Poi object cannot be null");
        }

        int count = poiRepo.updateInBatch(poiIds, poiMapper.toPoi(poi)) ;

        return OperationResult.of(count);
    }
    
}
