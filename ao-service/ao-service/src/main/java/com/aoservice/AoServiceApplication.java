package com.aoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AoServiceApplication.class, args);
	}

}
