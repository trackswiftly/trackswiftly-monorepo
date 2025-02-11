package com.trackswiftly.vehicle_service.dao.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.vehicle_service.dao.interfaces.BaseDao;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.utils.TenantContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@Repository
public class GroupRepo implements BaseDao<Group,Long>{

    @Value("${hibernate.jdbc.batch_size}")
    private  int batchSize = 10;

    
    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Group> insertInBatch(List<Group> entities) {

        log.debug("batch size is : {} 🔖\n" , batchSize);

        if (entities == null || entities.isEmpty()) {
            return entities ;
        }

        for (int i = 0; i < entities.size(); i++) {
            
            em.persist(entities.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

        return entities ;
        
    }


    

    @Override
    public int deleteByIds(List<Long> ids) {
        
        if (ids == null || ids .isEmpty()) {
            return 0;
        }

        String jpql = "DELETE FROM Department d WHERE d.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", ids   )
                    .executeUpdate() ;
    }



    @Override
    public List<Group> findByIds(List<Long> ids) {
        
        if (ids == null || ids.isEmpty()) {
            
            return Collections.emptyList();
        }
    
        String jpql = "SELECT d FROM Department d WHERE d.id IN :ids";
        
        return em.createQuery(jpql, Group.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }




    @Override
    public List<Group> findWithPagination(int page, int pageSize) {
        
        String jpql = "SELECT d FROM Department d ORDER BY d.id";

        TypedQuery<Group> query = em.createQuery(jpql, Group.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Long count() {
        
        String jpql = "SELECT COUNT(d) FROM Department d";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }
    
}
