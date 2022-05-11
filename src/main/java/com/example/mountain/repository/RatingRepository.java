package com.example.mountain.repository;


import com.example.mountain.entity.RatingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RatingRepository extends ReactiveSortingRepository<RatingEntity,Long> {

    Flux<RatingEntity> findByMountainId(Long mountainId, Pageable pageable);

    Mono<RatingEntity> findById(Long id);

    Mono<RatingEntity> save(RatingEntity ratingEntity);

    Mono<Void> deleteById(Long id);

    @Modifying
    @Query("UPDATE RATINGS SET rating_num = rating_num + 1 where id = :#{[0]}")
    Mono<Integer> addRatingNum(Long ratingId);

    @Modifying
    @Query("UPDATE RATINGS SET rating_num = rating_num -1 where id = :#{[0]}")
    Mono<Integer> subRatingNum(Long ratingId);
}
