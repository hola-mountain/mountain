package com.example.mountain.handler;

import com.example.mountain.dto.req.FavoriteReq;
import com.example.mountain.dto.req.RatingRecommendReq;
import com.example.mountain.dto.resp.FavoriteMountainResp;
import com.example.mountain.dto.resp.RatingResp;
import com.example.mountain.entity.FavoriteEntity;
import com.example.mountain.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FavoriteHandler {

    private final FavoriteService favoriteService;

    public Mono<ServerResponse> addFavorite(ServerRequest serverRequest){

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Mono<FavoriteEntity> favoriteEntityMono  = serverRequest.bodyToMono(FavoriteReq.class)
                .flatMap(r-> favoriteService.addFavorite(r.getUserId(),mountainId));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(favoriteEntityMono, FavoriteEntity.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> deleteFavorite(ServerRequest serverRequest){

        Long favoriteId = Long.parseLong(serverRequest.pathVariable("favoriteId"));

        Mono<Void> favoriteEntityMono  = serverRequest.bodyToMono(FavoriteReq.class)
                .flatMap(r-> favoriteService.removeFavorite(r.getUserId(), favoriteId));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(favoriteEntityMono, FavoriteEntity.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getMyPageFavorite(ServerRequest serverRequest){
        Long userId= Long.parseLong(serverRequest.queryParam("userId").get());

        Flux<FavoriteMountainResp> result = favoriteService.getMyPageFavoriteMountain(userId);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, FavoriteMountainResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getMountainDetailFavorite(ServerRequest serverRequest){
        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Long userId= Long.parseLong(serverRequest.queryParam("userId").get());
        Mono<FavoriteMountainResp> result = favoriteService.getMountainDetailFavorite(mountainId, userId);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, FavoriteMountainResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }
}
