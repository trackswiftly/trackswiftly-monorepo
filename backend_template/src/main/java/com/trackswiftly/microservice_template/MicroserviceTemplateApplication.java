package com.trackswiftly.microservice_template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MicroserviceTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceTemplateApplication.class, args);
	}

}
