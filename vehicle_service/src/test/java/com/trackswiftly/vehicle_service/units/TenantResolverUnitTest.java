package com.trackswiftly.vehicle_service.units;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trackswiftly.vehicle_service.utils.CurrentTenantIdentifierResolverImpl;
import com.trackswiftly.vehicle_service.utils.TenantContext;

import jakarta.persistence.EntityManager;





// @Tag("unit")
// @Tag("tenant")
@ExtendWith(MockitoExtension.class)
class TenantResolverUnitTest {
    


    @Mock
    private EntityManager entityManager;
    
    @Mock
    private Session session;
    
    @Mock
    private Filter filter;
    
    @InjectMocks
    private CurrentTenantIdentifierResolverImpl tenantResolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testResolveCurrentTenantIdentifier() {

        String expectedTenantId = "test-tenant";
        TenantContext.setTenantId(expectedTenantId);

        String resolvedTenantId = tenantResolver.resolveCurrentTenantIdentifier();

        assertEquals(expectedTenantId, resolvedTenantId);

        TenantContext.clear();
    }
}
