package com.trackswiftly.vehicle_service.integration;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.trackswiftly.vehicle_service.exception.UnableToProccessIteamException;


import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.Tag;



@Slf4j
@Tag("integration")
@Tag("tenant")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MultiTenantQueryTest {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testGeneratedQueryContainsTenantId() {
        Exception exception = assertThrows(UnableToProccessIteamException.class, () -> {
            entityManager.createQuery("SELECT v FROM TestMultiTenantEntity v").getResultList();
        });

        // Assert that the exception message contains "Generated query does not contain tenant_id"
        assert exception.getMessage().contains("Generated query does not contain tenant_id");
    }
}
