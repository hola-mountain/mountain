package com.example.mountain.router;

import com.example.mountain.handler.FavoriteHandler;
import com.example.mountain.handler.MountainHandler;
import com.example.mountain.service.FavoriteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class FavoriteRouter {

    @Bean
    public RouterFunction<ServerResponse> favoritesRouter(FavoriteHandler favoriteHandler){
        return
                RouterFunctions.route()
                        .POST("/mountain/{mountainId}/favorite", favoriteHandler::addFavorite)
                        .DELETE("/mountain/{mountainId}/favorite/{favoriteId}", favoriteHandler::deleteFavorite)
                        .build();
    }
}
