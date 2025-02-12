package com.trackswiftly.vehicle_service.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.trackswiftly.vehicle_service.utils.CurrentTenantIdentifierResolverImpl;
import com.trackswiftly.vehicle_service.utils.PropertiesLoader;
import com.zaxxer.hikari.HikariDataSource;

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


    // @Bean
    // public DataSource dataSource() {
    //     HikariDataSource dataSource = new HikariDataSource();
    //     dataSource.setJdbcUrl(resolveJdbcUrl());
    //     dataSource.setUsername(env.getProperty("DB_USER", "keycloak_user"));
    //     dataSource.setPassword(env.getProperty("DB_PASSWORD", "incorrect_password"));
    //     dataSource.setDriverClassName("org.postgresql.Driver");
    //     dataSource.setMinimumIdle(20);
    //     dataSource.setMaximumPoolSize(50);
    //     dataSource.setIdleTimeout(30000);
    //     dataSource.setConnectionTimeout(20000);
    //     dataSource.setMaxLifetime(1200000);
    //     return dataSource;
    // }

    // @Bean
    // public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

    //     // Properties properties = propertiesLoader.loadProperties("src/main/resources/application.properties");

    //     // properties.put("hibernate.multiTenancy", "DISCRIMINATOR");
    //     // properties.put("hibernate.tenant_identifier_resolver", tenantIdentifierResolver);
    //     // // properties.put("hibernate.hikari.dataSource.url", resolveJdbcUrl());
    //     // // properties.put("hibernate.hikari.dataSource.use", env.getProperty("DB_PASSWORD" , "incorrect_password")); 
    //     // // properties.put("hibernate.hikari.dataSource.password", env.getProperty("DB_USER" , "keycloak_user"));
    //     // // properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");

    //     LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

    //     emf.setDataSource(dataSource);
    //     emf.setPackagesToScan("com.trackswiftly.vehicle_service.entities");
        
    //     Properties properties = new Properties();
    //     properties.put("hibernate.multiTenancy", "DISCRIMINATOR");
    //     properties.put("hibernate.tenant_identifier_resolver", tenantIdentifierResolver);
    //     properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    //     properties.put("hibernate.hbm2ddl.auto", "update");
    //     properties.put("hibernate.show_sql", "true");
    //     properties.put("hibernate.format_sql", "true");
    //     properties.put("hibernate.use_sql_comments", "true");
    //     properties.put("hibernate.jdbc.batch_size", "20");
    //     properties.put("hibernate.order_inserts", "true");
    //     properties.put("hibernate.order_updates", "true");
    //     properties.put("hibernate.jdbc.batch_versioned_data", "true");
    //     properties.put("hibernate.id.optimizer.pooled.preferred", "pooled-lo");

    //     emf.setJpaProperties(properties);

    //     HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

    //     vendorAdapter.setGenerateDdl(true);  // Enable DDL generation
    //     vendorAdapter.setShowSql(true);      // Show SQL in logs
    //     vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
    //     emf.setJpaVendorAdapter(vendorAdapter);

    //     return emf;
    // }



    @Bean
    public PlatformTransactionManager transcationManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager() ;
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager ;
    }



    private String resolveJdbcUrl() {
        String urlTemplate = env.getProperty("jdbc.url.template");
        if (urlTemplate == null) {
            throw new IllegalStateException("JDBC URL template is not configured. Property 'jdbc.url.template' is missing.");
        }
    
        String dbHost = env.getProperty("DB_HOST");
        String dbName = env.getProperty("DB_NAME");
        String dbSchema = env.getProperty("DB_SCHEMA");
    
        // Validate required properties
        if (dbHost == null || dbName == null || dbSchema == null) {
            throw new IllegalStateException(String.format(
                "Missing required database properties. DB_HOST: %s, DB_NAME: %s, DB_SCHEMA: %s",
                dbHost == null ? MISSING : PRESENT ,
                dbName == null ? MISSING : PRESENT ,
                dbSchema == null ? MISSING : PRESENT 
            ));
        }
    
        return urlTemplate
                .replace("${DB_HOST}", dbHost)
                .replace("${DB_NAME}", dbName)
                .replace("${DB_SCHEMA}", dbSchema);
    }
}
