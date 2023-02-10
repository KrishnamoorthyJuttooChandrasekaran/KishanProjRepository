package com.ust.kishon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FarmerServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(FarmerServiceApplication.class, args);
		System.out.println("Hello");
		System.out.println("Application is running");
	}

}
