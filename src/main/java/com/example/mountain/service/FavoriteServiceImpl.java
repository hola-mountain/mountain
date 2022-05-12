package com.example.mountain.service;

import com.example.mountain.entity.FavoriteEntity;
import com.example.mountain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;

    @Override
    public Mono<FavoriteEntity> addFavorite(Long userId, Long mountainId) {
        return favoriteRepository.save(new FavoriteEntity(mountainId, userId));
    }

    @Override
    public Mono<Void> removeFavorite(Long userId, Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .filter(f-> userId == f.getUserId())
                .flatMap(f-> favoriteRepository.deleteById(favoriteId));
    }
}
