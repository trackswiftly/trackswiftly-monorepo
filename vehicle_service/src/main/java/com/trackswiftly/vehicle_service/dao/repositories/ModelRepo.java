package com.trackswiftly.vehicle_service.dao.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.vehicle_service.dao.interfaces.BaseDao;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.utils.DBUtiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@Repository
public class ModelRepo implements BaseDao<Model , Long>{

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize ;

    
    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Model> insertInBatch(List<Model> entities) {
        
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

        String jpql = "DELETE FROM Model d WHERE d.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", ids   )
                    .executeUpdate() ;
    }

    @Override
    public List<Model> findByIds(List<Long> ids) {
        
        if (ids == null || ids.isEmpty()) {
            
            return Collections.emptyList();
        }
    
        String jpql = "SELECT d FROM Model d WHERE d.id IN :ids";
        
        return em.createQuery(jpql, Model.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }

    @Override
    public List<Model> findWithPagination(int page, int pageSize) {
        
        String jpql = "SELECT d FROM Model d ORDER BY d.id";

        TypedQuery<Model> query = em.createQuery(jpql, Model.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Long count() {
        
        String jpql = "SELECT COUNT(d) FROM Model d";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }

    @Override
    public int updateInBatch(List<Long> ids, Model entity) {
        
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
    
}
