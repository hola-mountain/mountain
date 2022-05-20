package com.example.mountain.service;

import com.example.mountain.dto.resp.FavoriteMountainResp;
import com.example.mountain.entity.FavoriteEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavoriteService {

    Mono<FavoriteEntity> addFavorite(Long userId, Long mountainId);

    Flux<Void> removeFavorite(Long userId, Long mountainId);

    Flux<FavoriteMountainResp> getMyPageFavoriteMountain(Long userId);

    Mono<FavoriteMountainResp> getMountainDetailFavorite(Long mountainId, Long userId);
}
