package com.trackswiftly.vehicle_service.dao.repositories;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Repository
public class TestRepo {


    @PersistenceContext
    private EntityManager em ;


    @Transactional
    public void testGeneratedQueryContainsTenantId() {
        log.info("SELECT v FROM TestMultiTenantEntity v");
        em.createQuery("SELECT v FROM TestMultiTenantEntity v").getResultList();
    }
    
}
