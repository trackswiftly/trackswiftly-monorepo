package com.trackswiftly.vehicle_service.conf;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider ;


public class VehicleServiceHintsConfig implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        hints.resources().registerPattern("*.pub");
    }
    
}
