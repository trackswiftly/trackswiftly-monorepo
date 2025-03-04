package com.trackswiftly.vehicle_service.conf;

import java.util.UUID;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import com.trackswiftly.vehicle_service.utils.TenantStatementInspector;

import liquibase.ui.LoggerUIService;
import liquibase.database.LiquibaseTableNamesFactory;
import liquibase.report.ShowSummaryGeneratorFactory ;
import liquibase.parser.SqlParserFactory;

import liquibase.changelog.visitor.ValidatingVisitorGeneratorFactory ;
import liquibase.changelog.FastCheckService ;


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
        hints.reflection().registerType(SqlParserFactory.class, MemberCategory.values()) ;


        hints.reflection().registerType(TenantStatementInspector.class, MemberCategory.values()) ;
        hints.reflection().registerType(UUID[].class, MemberCategory.values()) ;

        /**
         * 
         * Liquibase
         */
        hints.reflection().registerType(ValidatingVisitorGeneratorFactory.class, MemberCategory.values()) ;
        hints.reflection().registerType(FastCheckService.class, MemberCategory.values()) ;

    }
    
}
