package com.trackswiftly.client_service.dao.repositories;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.client_service.dao.interfaces.BaseDao;
import com.trackswiftly.client_service.entities.Poi;
import com.trackswiftly.client_service.utils.DBUtiles;
import com.trackswiftly.utils.base.utils.TenantContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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
        
        if (ids == null || ids .isEmpty()) {
            return 0;
        }

        String jpql = "DELETE FROM Poi d WHERE d.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", ids   )
                    .executeUpdate() ;
    }

    @Override
    public List<Poi> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            
            return Collections.emptyList();
        }
    
        String jpql = "SELECT d FROM Poi d WHERE d.id IN :ids";
        
        return em.createQuery(jpql, Poi.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }

    @Override
    public List<Poi> findWithPagination(int page, int pageSize) {
        String jpql = "SELECT d FROM Poi d ORDER BY d.id";

        TypedQuery<Poi> query = em.createQuery(jpql, Poi.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Long count() {
        String jpql = "SELECT COUNT(d) FROM Poi d";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }


    @Override
    public int updateInBatch(List<Long> ids, Poi entity) {
        int totalUpdatedRecords = 0 ;

        Query query = DBUtiles.buildJPQLQueryDynamicallyForUpdate(entity, em) ;


        for (int i = 0; i < ids.size(); i += batchSize) {
            List<Long> batch = ids.subList(i, Math.min(i + batchSize, ids.size()));
    
            
            query.setParameter("Ids", batch);

            // Execute the update
            int updatedRecords = query.executeUpdate();
            totalUpdatedRecords += updatedRecords;
    
            em.flush();
            em.clear();

        }

        return totalUpdatedRecords ;
    }




    public <T> long countBasedOnIds(Class<T> entityClass, Set<Long> ids) {

        log.info("Tenant Id : {} 🇲🇦" , TenantContext.getTenantId());

        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e.id IN :ids";
        return em.createQuery(jpql, Long.class)
                 .setParameter("ids", ids)
                 .getSingleResult();
    }
    
}
