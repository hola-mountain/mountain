package com.example.mountain.repository;

import com.example.mountain.entity.RatingRecEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface RatingRecRepository extends ReactiveSortingRepository<RatingRecEntity,Long> {

    @Modifying
    @Query("DELETE RATING_REC where user_id = :#{[0]} and rating_id = :#{[1]}")
    Mono<Void> deleteByUserIdAndRatingId(Long userId, Long ratingId);

    @Query("SELECT id FROM RATING_REC  where user_id = :#{[0]} and rating_id = :#{[1]}")
    Mono<RatingRecEntity> findByUserIdAndRatingId(Long userId, Long ratingId);
}
