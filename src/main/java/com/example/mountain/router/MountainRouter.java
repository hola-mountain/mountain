package com.example.mountain.router;

import com.example.mountain.dto.resp.MountainResp;
import com.example.mountain.handler.MountainHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class MountainRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/mountain"
                    , produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = MountainHandler.class, beanMethod = "getMountainList",
                    operation = @Operation(operationId = "getMountainList", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MountainResp.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee ID supplied"),
                            @ApiResponse(responseCode = "404", description = "Employee not found")}, parameters = {
                            @Parameter(name = "pageNum", description = "페이지 번호",
                                    in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                            @Parameter(name = "pageSize", description = "보여지는 페이지 수",
                                    in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                            @Parameter(name = "district", description = "지역코드",
                                    in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                            @Parameter(name = "isAsc", description = "정렬 방향",
                                    in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                            @Parameter(name = "sort", description = "sort specification",
                                    in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                            @Parameter(in = ParameterIn.PATH, name = "mountainId")}
                            )
            ),
            @RouterOperation(path = "/mountain/{mountainId}", produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = MountainHandler.class, beanMethod = "getMountainDetail",
                    operation = @Operation(operationId = "getMountainDetail", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MountainResp.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee details supplied")}
                    )),

    })
    public RouterFunction<ServerResponse> mountainsRouter(MountainHandler mountainHandler){
        return
                RouterFunctions.route()
                                .GET("/mountain", mountainHandler::getMountainList)
                                .GET("/mountain/{mountainId}", mountainHandler::getMountainDetail)
                                .build();
    }

}
