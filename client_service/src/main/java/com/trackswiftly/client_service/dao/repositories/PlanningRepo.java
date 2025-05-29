package com.trackswiftly.client_service.dao.repositories;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.client_service.dao.interfaces.BaseDao;
import com.trackswiftly.client_service.entities.PlanningEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Repository
public class PlanningRepo implements BaseDao<PlanningEntity,Long>{


    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize ;

    
    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<PlanningEntity> insertInBatch(List<PlanningEntity> entities) {

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
    public List<PlanningEntity> findByIds(List<Long> ids) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIds'");
    }

    @Override
    public List<PlanningEntity> findWithPagination(int page, int pageSize) {
        throw new UnsupportedOperationException("Unimplemented method 'findWithPagination'");
    }

    @Override
    public Long count() {
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public int updateInBatch(List<Long> ids, PlanningEntity entity) {
        throw new UnsupportedOperationException("Unimplemented method 'updateInBatch'");
    }

    @Override
    public List<PlanningEntity> search(String keyword, int page, int pageSize) {
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }



    public List<PlanningEntity> findByTimeRange(OffsetDateTime from, OffsetDateTime to) {
        
        String jpql = "SELECT p FROM PlanningEntity p WHERE p.createdAt BETWEEN :from AND :to";

        TypedQuery<PlanningEntity> query = em.createQuery(jpql, PlanningEntity.class);
        query.setParameter("from", from);
        query.setParameter("to", to);

        return query.getResultList();
    }
    
}
