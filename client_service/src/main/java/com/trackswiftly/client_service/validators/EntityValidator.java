package com.trackswiftly.client_service.validators;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.trackswiftly.client_service.exception.UnableToProccessIteamException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityValidator {


    @PersistenceContext
    private EntityManager em;

    
}
