package com.example.mountain.router;


import com.example.mountain.handler.RatingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class RatingRouter {

    @Bean
    public RouterFunction<ServerResponse> reivewsRouter(RatingHandler ratingHandler){
         return
                RouterFunctions.route()
                        .GET("/mountain/{mountainId}/review", ratingHandler::getReviewInMountainDetail)
                        .POST("/mountain/{mountainId}/review", ratingHandler::createReview)
                        .PUT("/mountain/{mountainId}/review/{ratingId}", ratingHandler::modifyReview)
                        .DELETE("/mountain/{mountainId}/review/{ratingId}", ratingHandler::removeReview)
                        .PATCH("/mountain/{mountainId}/review/{ratingId}/recommend", ratingHandler::recommendReview)
                        .build();
    }
}
