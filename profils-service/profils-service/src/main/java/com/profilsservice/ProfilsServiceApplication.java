package com.profilsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
public class ProfilsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfilsServiceApplication.class, args);
	}

}
