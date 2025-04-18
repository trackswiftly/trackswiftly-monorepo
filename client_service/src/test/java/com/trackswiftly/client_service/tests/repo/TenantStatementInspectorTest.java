package com.trackswiftly.client_service.tests.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trackswiftly.client_service.utils.TenantStatementInspector;
import com.trackswiftly.utils.exception.UnableToProccessIteamException;


@DisabledInNativeImage
@ExtendWith(MockitoExtension.class)
class TenantStatementInspectorTest {


    @InjectMocks
    private TenantStatementInspector tenantStatementInspector;


    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;



    @Test
    void testStatementInspectorIsCalled() {
        String sql = "SELECT * FROM POI";

        UnableToProccessIteamException exception = assertThrows(UnableToProccessIteamException.class, () -> {
            tenantStatementInspector.inspect(sql);
        });


        assertEquals("Generated query does not contain tenant_id condition: SELECT * FROM POI", exception.getMessage());
    }

}
