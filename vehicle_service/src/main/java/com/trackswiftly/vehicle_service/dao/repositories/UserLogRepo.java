package com.trackswiftly.vehicle_service.dao.repositories;

import org.springframework.stereotype.Repository;

import com.trackswiftly.vehicle_service.entities.UserLog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class UserLogRepo {
    

    @PersistenceContext
    private EntityManager em;



    @Transactional
    public void save(UserLog userLog) {
        em.persist(userLog);
    }



    @Transactional
    public void update(UserLog userLog) {
        em.merge(userLog);
    }
}
