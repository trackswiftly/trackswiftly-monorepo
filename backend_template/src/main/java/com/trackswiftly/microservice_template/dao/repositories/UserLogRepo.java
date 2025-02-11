package com.trackswiftly.microservice_template.dao.repositories;

import org.springframework.stereotype.Repository;

import com.trackswiftly.microservice_template.entities.UserLog;

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
