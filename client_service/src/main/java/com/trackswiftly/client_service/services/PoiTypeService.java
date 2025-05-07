package com.trackswiftly.client_service.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.client_service.dao.repositories.PoiTypeRepo;
import com.trackswiftly.client_service.dtos.PoiTypeRequest;
import com.trackswiftly.client_service.dtos.PoiTypeResponse;
import com.trackswiftly.client_service.mappers.PoiTypeMapper;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;
import com.trackswiftly.utils.interfaces.TrackSwiftlyServiceInterface;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
@Transactional
public class PoiTypeService implements TrackSwiftlyServiceInterface<Long , PoiTypeRequest, PoiTypeResponse> {

    private PoiTypeRepo poiTypeRepo ;
    private PoiTypeMapper poiTypeMapper ;


    @Autowired
    PoiTypeService(
        PoiTypeRepo poiTypeRepo ,
        PoiTypeMapper poiTypeMapper
    ) {
        this.poiTypeRepo = poiTypeRepo ;

        this.poiTypeMapper = poiTypeMapper ;
    }


    /**
     * 
     * ####################################################################
     */

    @Override
    public List<PoiTypeResponse> createEntities(List<PoiTypeRequest> poiTypeRequests) {
        log.info("Creating groups in batch...");

        return poiTypeMapper.toPoiTypeResponseList(
            poiTypeRepo.insertInBatch(
                poiTypeMapper.toPoiTypeList(
                    poiTypeRequests
                ) 
            )
        ) ;
    }



    @Override
    public OperationResult deleteEntities(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return OperationResult.of(0);
        }

        int count = poiTypeRepo.deleteByIds(ids); 

        return OperationResult.of(count) ;
    }



    @Override
    public List<PoiTypeResponse> findEntities(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return poiTypeMapper.toPoiTypeResponseList(
            poiTypeRepo.findByIds(ids)
        );
    }



    @Override
    public PageDTO<PoiTypeResponse> pageEntities(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive values");
        }


        List<PoiTypeResponse> content = poiTypeMapper.toPoiTypeResponseList( // mappe groups to groupsrespones
            poiTypeRepo.findWithPagination(page, pageSize) // get grooup data
        ) ;


        long totalElements = poiTypeRepo.count();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDTO<>(content, page, pageSize, totalElements, totalPages);
    }



    @Override
    public OperationResult updateEntities(List<Long> ids, PoiTypeRequest poiType) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("PoiType IDs list cannot be null or empty");
        }

        if (poiType == null) {
            throw new IllegalArgumentException("PoiType object cannot be null");
        }

        int count = poiTypeRepo.updateInBatch(ids, poiTypeMapper.toPoiType(poiType)) ;

        return OperationResult.of(count);
    }


    @Override
    public List<PoiTypeResponse> search(String keyword) {
        int page = 0;
        int pageSize = 20;

        return poiTypeMapper.toPoiTypeResponseList(
            poiTypeRepo.search(keyword, page, pageSize)
        );
    }
    
}