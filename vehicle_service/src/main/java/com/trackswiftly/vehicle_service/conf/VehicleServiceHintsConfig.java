package com.trackswiftly.vehicle_service.conf;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import liquibase.ui.LoggerUIService;
import liquibase.database.LiquibaseTableNamesFactory;
import liquibase.report.ShowSummaryGeneratorFactory ;



public class VehicleServiceHintsConfig implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        /**
         * 
         * registered resources
         */
        hints.resources().registerPattern("*.pub");


        /**
         * 
         * registered reflections
         * 
         */

        hints.reflection().registerType(LoggerUIService.class, MemberCategory.values()) ;
        hints.reflection().registerType(LiquibaseTableNamesFactory.class, MemberCategory.values()) ;
        hints.reflection().registerType(ShowSummaryGeneratorFactory.class, MemberCategory.values()) ;


    }
    
}
