package org.osbo.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import kong.unirest.core.Unirest;

@SpringBootApplication
public class MicroserviceApplication {

	public static void main(String[] args) {
		Unirest.config().connectTimeout(6000000);
		SpringApplication.run(MicroserviceApplication.class, args);
	}

}
