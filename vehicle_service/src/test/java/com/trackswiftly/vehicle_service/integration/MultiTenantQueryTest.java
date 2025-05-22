package com.trackswiftly.vehicle_service.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.trackswiftly.vehicle_service.VehicleServiceApplication;
import com.trackswiftly.vehicle_service.dao.repositories.TestRepo;
import com.trackswiftly.vehicle_service.exception.UnableToProccessIteamException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest
@SpringJUnitConfig(classes = {VehicleServiceApplication.class})
@Transactional
class MultiTenantQueryTest {
    

    private TestRepo testRepo;
    
    @Autowired
    MultiTenantQueryTest(
        TestRepo testRepo
    ) {
        this.testRepo = testRepo;
    }

    // @Test
    void testGeneratedQueryContainsTenantId() {
        Exception exception = assertThrows(UnableToProccessIteamException.class, () -> {
            testRepo.testGeneratedQueryContainsTenantId();
        });

        // Assert that the exception message contains "Generated query does not contain tenant_id"
        assert exception.getMessage().contains("Generated query does not contain tenant_id");
    }
}
