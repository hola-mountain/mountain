package com.example.mountain.router;

import com.example.mountain.dto.req.FavoriteReq;
import com.example.mountain.entity.FavoriteEntity;
import com.example.mountain.handler.FavoriteHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class FavoriteRouter {

    @RouterOperations({
            @RouterOperation(path = "/mountain/{mountainId}/favorite"
                    , produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST, beanClass = FavoriteHandler.class, beanMethod = "addFavorite",
                    operation = @Operation(operationId = "addFavorite", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = FavoriteEntity.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee ID supplied"),
                            @ApiResponse(responseCode = "404", description = "Employee not found")},
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "mountainId")},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = FavoriteReq.class)))
                    )
            ),
            @RouterOperation(path = "/mountain/{mountainId}/favorite/{favorite}", produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.DELETE, beanClass = FavoriteHandler.class, beanMethod = "deleteFavorite",
                    operation = @Operation(operationId = "deleteFavorite", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = FavoriteEntity.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee details supplied")}, parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "favoriteId"),
                            @Parameter(in = ParameterIn.PATH, name = "mountainId")},
                            requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = FavoriteReq.class)))
                    )),

    })
    @Bean
    public RouterFunction<ServerResponse> favoritesRouter(FavoriteHandler favoriteHandler){
        return
                RouterFunctions.route()
                        .POST("/mountain/{mountainId}/favorite", favoriteHandler::addFavorite)
                        .DELETE("/mountain/{mountainId}/favorite/{favoriteId}", favoriteHandler::deleteFavorite)
                        .GET("/mountain/favorite", favoriteHandler::getMyPageFavorite)
                        .build();
    }
}
