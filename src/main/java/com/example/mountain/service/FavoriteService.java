package com.example.mountain.service;

import com.example.mountain.entity.FavoriteEntity;
import reactor.core.publisher.Mono;

public interface FavoriteService {

    Mono<FavoriteEntity> addFavorite(Long userId, Long mountainId);

    Mono<Void> removeFavorite(Long userId, Long favoriteId);
}
