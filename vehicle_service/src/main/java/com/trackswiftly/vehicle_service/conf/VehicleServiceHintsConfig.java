package com.trackswiftly.vehicle_service.conf;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import com.fasterxml.jackson.databind.BeanDescription;
import com.trackswiftly.vehicle_service.utils.TenantStatementInspector;

import io.swagger.v3.core.jackson.mixin.Schema31Mixin;
import io.swagger.v3.oas.models.media.JsonSchema;
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



         /**
         * 
         * swagger config
         */

        hints.reflection().registerType(
            Schema31Mixin.TypeSerializer.class ,
            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.DECLARED_CLASSES
        ) ;


        hints.reflection().registerType(JsonSchema.class, MemberCategory.values());
        hints.reflection().registerType(BeanDescription.class, MemberCategory.values());

        // Add this if you still see reflection errors for Function itself
        hints.reflection().registerType(Function[].class, MemberCategory.values());

    }
    
}
