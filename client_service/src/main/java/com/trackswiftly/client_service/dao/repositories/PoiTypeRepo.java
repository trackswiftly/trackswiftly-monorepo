package com.trackswiftly.client_service.dao.repositories;




import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.client_service.dao.interfaces.BaseDao;
import com.trackswiftly.client_service.entities.PoiType;
import com.trackswiftly.client_service.utils.DBUtiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;





@Data
@Slf4j
@Repository
public class PoiTypeRepo implements BaseDao<PoiType , Long>{
    
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize = 40;

    
    @PersistenceContext
    private EntityManager em ;


    @Override
    public List<PoiType> insertInBatch(List<PoiType> entities) {
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

        String jpql = "DELETE FROM PoiType d WHERE d.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", ids   )
                    .executeUpdate() ;
    }


    @Override
    public List<PoiType> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            
            return Collections.emptyList();
        }
    
        String jpql = "SELECT d FROM PoiType d WHERE d.id IN :ids";
        
        return em.createQuery(jpql, PoiType.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }


    @Override
    public List<PoiType> findWithPagination(int page, int pageSize) {
        String jpql = "SELECT d FROM PoiType d ORDER BY d.id";

        TypedQuery<PoiType> query = em.createQuery(jpql, PoiType.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }


    @Override
    public Long count() {
        String jpql = "SELECT COUNT(d) FROM PoiType d";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }


    @Override
    public int updateInBatch(List<Long> ids, PoiType entity) {
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


    @Override
    public List<PoiType> search(String keyword, int page, int pageSize) {
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }



    
    
    
}
