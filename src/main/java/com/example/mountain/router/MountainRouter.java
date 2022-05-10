package com.example.mountain.router;

import com.example.mountain.handler.MountainHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class MountainRouter {

    @Bean
    public RouterFunction<ServerResponse> mountainsRouter(MountainHandler mountainHandler){
        return
                RouterFunctions.route()
                                .GET("/mountain", mountainHandler::getMountainList)
                                .GET("/mountain/{mountainId}", mountainHandler::getMountainDetail)
                                .build();
    }

}
