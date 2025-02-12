package com.trackswiftly.vehicle_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.trackswiftly.vehicle_service.conf.VehicleServiceHintsConfig;

@SpringBootApplication
@EnableAspectJAutoProxy
@ImportRuntimeHints(VehicleServiceHintsConfig.class)
public class VehicleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleServiceApplication.class, args);
	}

}
