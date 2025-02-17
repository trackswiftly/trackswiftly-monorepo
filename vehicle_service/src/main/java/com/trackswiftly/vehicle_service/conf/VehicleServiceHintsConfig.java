package com.trackswiftly.vehicle_service.conf;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;


public class VehicleServiceHintsConfig implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        hints.resources().registerPattern("*.pub");
    }
    
}
