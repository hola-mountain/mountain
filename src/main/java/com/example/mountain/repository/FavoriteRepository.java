package com.example.mountain.repository;

import com.example.mountain.entity.FavoriteEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface FavoriteRepository extends ReactiveSortingRepository<FavoriteEntity,Long> {
    Flux<FavoriteEntity> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT id, mountain_id, user_id, regdate FROM FAVORITES WHERE user_id =:userId Order By regdate DESC")
    Flux<FavoriteEntity> findByUserId(Long userId);

}
