package com.trackswiftly.vehicle_service.conf;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;

import com.trackswiftly.vehicle_service.utils.PropertiesLoader;


@Configuration
public class UtilsConf {


        @Value("${server.servlet.context-path:}")
        private String contextPath;
    
        @Bean("propertiesLoader1")
        public PropertiesLoader propertiesLoader() {
                return PropertiesLoader.builder().build() ;
        }


        /***
         * 
         * swagger config
         */

        @SuppressWarnings("unchecked")
        @Bean
        public OpenAPI customOpenAPI() {
        
                String serverUrl = contextPath.equals("/") ? "" : contextPath;

                return new OpenAPI()
                .addServersItem(new Server().url(serverUrl).description("Server URL"))
                .info(new Info()
                        .title("TrackSwiftly TMS Vehicle Service ")
                        .description("Multi-tenant vehicle service for the TMS Platform")
                        .version("v1.0.0")
                )
                // Add other OpenAPI configurations (security, components, etc.)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
        }

    
}
