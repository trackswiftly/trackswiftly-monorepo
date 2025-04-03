package com.trackswiftly.client_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.trackswiftly.client_service.conf.ClientHintsConfig;


@SpringBootApplication
@ImportRuntimeHints(ClientHintsConfig.class)
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

}
