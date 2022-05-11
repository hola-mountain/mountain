package com.example.mountain.handler;

import com.example.mountain.dto.req.RatingRecommendReq;
import com.example.mountain.dto.req.RatingRequest;
import com.example.mountain.dto.resp.RatingResp;
import com.example.mountain.entity.RatingEntity;
import com.example.mountain.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RatingHandler {

    private final RatingService ratingService;

    public Mono<ServerResponse> getReviewInMountainDetail(ServerRequest serverRequest){

        int pageNum = Integer.parseInt(serverRequest.queryParam("pageNum").get());
        int pageSize = Integer.parseInt(serverRequest.queryParam("pageSize").get());
        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));

        Flux<RatingResp> result = ratingService.getRatingListPage(mountainId,pageNum,pageSize);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> createReview(ServerRequest serverRequest){

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Mono<RatingEntity> ratingMono = serverRequest.bodyToMono(RatingRequest.class)
                .onErrorResume(throwable -> {
                    return Mono.error(new RuntimeException(throwable));
                }).flatMap( r-> {
                   return ratingService.createRating(RatingEntity.builder()
                                    .title(r.getTitle())
                                    .comment(r.getComment())
                                    .thumbImg(r.getThumbImg())
                                    .star(r.getStar())
                                    .mountainId(mountainId)
                                    .userId(r.getUserId())
                                    .ratingNum(0L)
                                    .nickname(r.getNickname()).build());
                });

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingMono, RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> modifyReview(ServerRequest serverRequest){

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Long ratingId = Long.parseLong(serverRequest.pathVariable("ratingId"));

        Mono<RatingEntity> ratingMono = serverRequest.bodyToMono(RatingRequest.class)
                .onErrorResume(throwable -> {
                    return Mono.error(new RuntimeException(throwable));
                }).flatMap( r-> {
                    return ratingService.updateRating(r, ratingId);
                });

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingMono, RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> removeReview(ServerRequest serverRequest){

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Long ratingId = Long.parseLong(serverRequest.pathVariable("ratingId"));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingService.deleteRating(ratingId), RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> recommendReview(ServerRequest serverRequest){

        Long ratingId = Long.parseLong(serverRequest.pathVariable("ratingId"));
        Mono<Integer> ratingMono = serverRequest.bodyToMono(RatingRecommendReq.class)
                .flatMap(r-> ratingService.toggleRecommend(ratingId, r.getUserId(), r.getAdd()));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingMono, RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

}
