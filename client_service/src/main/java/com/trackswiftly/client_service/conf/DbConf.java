package com.trackswiftly.client_service.conf;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class DbConf {

    @Bean
    public PlatformTransactionManager transcationManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager() ;
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager ;
    }


}
