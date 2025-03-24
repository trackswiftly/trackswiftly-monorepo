package com.trackswiftly.client_service.dao.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.client_service.dao.interfaces.BaseDao;
import com.trackswiftly.client_service.entities.Poi;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@Repository
public class PoiRepo implements BaseDao<Poi,Long>{


    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize ;

    
    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Poi> insertInBatch(List<Poi> entities) {
        log.debug("batch size is : {} 🔖\n" , batchSize);

        if (entities == null || entities.isEmpty()) {
            return entities ;
        }

        for (int i = 0; i < entities.size(); i++) {
            
            em.persist(entities.get(i));

            if (i > 0 && (i + 1) % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }


        // Flush and clear the remaining entities that didn't make up a full batch
        if (entities.size() % batchSize != 0) {
            em.flush();
            em.clear();
        }

        return entities ;
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByIds'");
    }

    @Override
    public List<Poi> findByIds(List<Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIds'");
    }

    @Override
    public List<Poi> findWithPagination(int page, int pageSize) {
        throw new UnsupportedOperationException("Unimplemented method 'findWithPagination'");
    }

    @Override
    public Long count() {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public int updateInBatch(List<Long> ids, Poi entity) {
        throw new UnsupportedOperationException("Unimplemented method 'updateInBatch'");
    }
    
}
