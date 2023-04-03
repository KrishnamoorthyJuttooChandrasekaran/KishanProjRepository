package com.ust.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistoryApplication.class, args);
    }

}
