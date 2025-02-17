package com.trackswiftly.vehicle_service.dao.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.vehicle_service.dao.interfaces.BaseDao;
import com.trackswiftly.vehicle_service.entities.Vehicle;
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
public class VehicleRepo implements BaseDao<Vehicle , Long>{


    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize ;

    
    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Vehicle> insertInBatch(List<Vehicle> entities) {
        
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

        String jpql = "DELETE FROM Vehicle d WHERE d.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", ids   )
                    .executeUpdate() ;
    }

    @Override
    public List<Vehicle> findByIds(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            
            return Collections.emptyList();
        }
    
        String jpql = "SELECT d FROM Vehicle d WHERE d.id IN :ids";
        
        return em.createQuery(jpql, Vehicle.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }

    @Override
    public List<Vehicle> findWithPagination(int page, int pageSize) {
        String jpql = "SELECT d FROM Vehicle d ORDER BY d.id";

        TypedQuery<Vehicle> query = em.createQuery(jpql, Vehicle.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Long count() {
        String jpql = "SELECT COUNT(d) FROM Vehicle d";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }

    @Override
    public int updateInBatch(List<Long> ids, Vehicle entity) {
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
