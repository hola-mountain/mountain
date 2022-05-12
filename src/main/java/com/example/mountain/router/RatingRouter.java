package com.example.mountain.router;


import com.example.mountain.dto.req.FavoriteReq;
import com.example.mountain.dto.req.RatingRecommendReq;
import com.example.mountain.dto.req.RatingRequest;
import com.example.mountain.dto.resp.RatingResp;
import com.example.mountain.entity.FavoriteEntity;
import com.example.mountain.handler.FavoriteHandler;
import com.example.mountain.handler.RatingHandler;
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
public class RatingRouter {


    @RouterOperations({
            @RouterOperation(path = "/mountain/{mountainId}/review"
                    , produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET, beanClass = RatingHandler.class, beanMethod = "getReviewInMountainDetail",
                    operation = @Operation(operationId = "getReviewInMountainDetail", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatingResp.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee ID supplied"),
                            @ApiResponse(responseCode = "404", description = "Employee not found")},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "mountainId"),
                                    @Parameter(name = "pageNum", description = "페이지 번호",
                                            in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                                    @Parameter(name = "pageSize", description = "보여지는 페이지 수",
                                            in = ParameterIn.QUERY, schema = @Schema(type = "string")),
                            },
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RatingRequest.class)))
                    )
            ),
            @RouterOperation(path = "/mountain/{mountainId}/review", produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST, beanClass = RatingHandler.class, beanMethod = "createReview",
                    operation = @Operation(operationId = "createReview", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatingResp.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee details supplied")}, parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "mountainId")},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RatingRequest.class)))
                    )),
            @RouterOperation(path = "/mountain/{mountainId}/review/{ratingId}", produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.PUT, beanClass = RatingHandler.class, beanMethod = "modifyReview",
                    operation = @Operation(operationId = "modifyReview", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatingResp.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee details supplied")}, parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "mountainId"),
                            @Parameter(in = ParameterIn.PATH, name = "ratingId")},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RatingRequest.class)))
                    )),
            @RouterOperation(path = "/mountain/{mountainId}/review/{ratingId}", produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.DELETE, beanClass = RatingHandler.class, beanMethod = "removeReview",
                    operation = @Operation(operationId = "removeReview", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatingResp.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee details supplied")}, parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "mountainId"),
                            @Parameter(in = ParameterIn.PATH, name = "ratingId")},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RatingRequest.class)))
                    )),
            @RouterOperation(path = "/mountain/{mountainId}/review/{ratingId}/recommend", produces = {
                    MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.PATCH, beanClass = RatingHandler.class, beanMethod = "recommendReview",
                    operation = @Operation(operationId = "recommendReview", responses = {
                            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RatingResp.class))),
                            @ApiResponse(responseCode = "400", description = "Invalid Employee details supplied")}, parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "mountainId"),
                            @Parameter(in = ParameterIn.PATH, name = "ratingId")},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RatingRecommendReq.class)))
                    )),

    })
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
