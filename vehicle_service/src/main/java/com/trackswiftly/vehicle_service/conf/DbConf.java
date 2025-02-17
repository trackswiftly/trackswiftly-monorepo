package com.trackswiftly.vehicle_service.conf;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.trackswiftly.vehicle_service.utils.CurrentTenantIdentifierResolverImpl;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class DbConf {

    private static final String MISSING = "missing" ;
    private static final String  PRESENT = "present" ;
    
    private Environment env;

    private CurrentTenantIdentifierResolverImpl tenantIdentifierResolver;
    
    
    @Autowired
    DbConf(
        Environment env ,
        CurrentTenantIdentifierResolverImpl tenantIdentifierResolver
    ) {

        this.env = env ;
        this.tenantIdentifierResolver = tenantIdentifierResolver ;
    }




    @Bean
    public PlatformTransactionManager transcationManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager() ;
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager ;
    }


}
