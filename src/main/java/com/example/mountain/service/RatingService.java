package com.example.mountain.service;

import com.example.mountain.dto.req.RatingRequest;
import com.example.mountain.dto.resp.FavoriteMountainResp;
import com.example.mountain.dto.resp.RatingMountainResp;
import com.example.mountain.dto.resp.RatingResp;
import com.example.mountain.entity.RatingEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingService {

    public Flux<RatingResp> getRatingListPage(Long mountainId, int pageNum, int pageSize);

    public Mono<RatingEntity> createRating(RatingEntity ratingEntity);

    public Mono<RatingEntity> updateRating(RatingRequest ratingRequest, Long ratingId);

    public Mono<Void> deleteRating(Long id, Long userId);

    Mono<Integer> toggleRecommend(Long ratingId, Long userId, Integer add);

    Flux<RatingMountainResp> getMyPageReviewMountain(Long userId);
}
