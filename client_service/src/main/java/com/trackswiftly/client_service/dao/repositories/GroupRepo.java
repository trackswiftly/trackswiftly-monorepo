package com.trackswiftly.client_service.dao.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.trackswiftly.client_service.dao.interfaces.BaseDao;
import com.trackswiftly.client_service.entities.Group;
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
public class GroupRepo implements BaseDao<Group,Long>{

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize ;

    
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

        String jpql = "DELETE FROM Group d WHERE d.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", ids   )
                    .executeUpdate() ;
    }



    @Override
    public List<Group> findByIds(List<Long> ids) {
        
        if (ids == null || ids.isEmpty()) {
            
            return Collections.emptyList();
        }
    
        String jpql = "SELECT d FROM Group d WHERE d.id IN :ids";
        
        return em.createQuery(jpql, Group.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }




    @Override
    public List<Group> findWithPagination(int page, int pageSize) {
        
        String jpql = "SELECT d FROM Group d ORDER BY d.id";

        TypedQuery<Group> query = em.createQuery(jpql, Group.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Long count() {
        
        String jpql = "SELECT COUNT(d) FROM Group d";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }




    @Override
    public int updateInBatch(List<Long> ids, Group entity) {
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
    public List<Group> search(String keyword, int page, int pageSize) {

        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        String sql = """
                SELECT id, name, description, created_at, updated_at
                FROM groups
                WHERE tenant_id = :tenantId 
                    AND to_tsvector('english', coalesce(name, '') || ' ' || coalesce(description, '')) 
                        @@ websearch_to_tsquery('english', :keyword)
                ORDER BY 
                ts_rank_cd(
                    to_tsvector('english', coalesce(name, '') || ' ' || coalesce(description, '')), 
                    websearch_to_tsquery('english', :keyword)
                ) DESC
                LIMIT :limit OFFSET :offset
            """;

        @SuppressWarnings("unchecked")
        List<Group> results = em.createNativeQuery(sql, Group.class)
            .setParameter("tenantId", TenantContext.getTenantId())
            .setParameter("keyword", keyword)
            .setParameter("limit", pageSize)
            .setParameter("offset", page * pageSize)
            .getResultList();
        return results;
    }
}
