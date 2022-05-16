package com.example.mountain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MountainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MountainApplication.class, args);
    }

}
