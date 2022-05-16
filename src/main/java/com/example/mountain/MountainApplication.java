package com.example.mountain;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "MOUNTAIN SWAGGER", version = "1.0", description = "Documentation APIs v1.0"))
@EnableDiscoveryClient
public class MountainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MountainApplication.class, args);
    }

}
