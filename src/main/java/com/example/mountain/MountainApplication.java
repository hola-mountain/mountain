package com.example.mountain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MountainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MountainApplication.class, args);
    }

}
