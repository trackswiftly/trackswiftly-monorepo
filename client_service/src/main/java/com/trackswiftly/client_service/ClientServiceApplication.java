package com.trackswiftly.client_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.trackswiftly.client_service.conf.ClientHintsConfig;


@SpringBootApplication
@EnableFeignClients
@ImportRuntimeHints(ClientHintsConfig.class)
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

}
