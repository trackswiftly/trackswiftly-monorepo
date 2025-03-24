package com.trackswiftly.client_service.validators;


import org.springframework.stereotype.Component;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityValidator {


    @PersistenceContext
    private EntityManager em;

    
}
