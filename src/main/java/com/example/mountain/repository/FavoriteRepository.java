package com.example.mountain.repository;

import com.example.mountain.entity.FavoriteEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Locale;

@Repository
public interface FavoriteRepository extends ReactiveSortingRepository<FavoriteEntity,Long> {
//    Flux<FavoriteEntity> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT id, mountain_id, user_id, regdate FROM FAVORITES WHERE user_id =:userId Order By regdate DESC")
    Flux<FavoriteEntity> findByUserId(Long userId);

    @Query("SELECT id, mountain_id, user_id FROM FAVORITES WHERE mountain_id =:mountainId and user_id =:userId")
    Mono<FavoriteEntity> findByMountainIdAndUserId(Long mountainId, Long userId);

    @Query("DELETE FROM FAVORITES WHERE id =:id")
    Mono<Void> deleteById(Long id);

    @Query("SELECT id, mountain_id, user_id FROM FAVORITES WHERE user_id =:userId and mountain_id =:mountainId")
    Flux<FavoriteEntity> findByUserIdAndMountainId(Long userId, Long mountainId);

}
