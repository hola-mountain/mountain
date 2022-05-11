package com.example.mountain.service;

import com.example.mountain.dto.req.RatingRequest;
import com.example.mountain.dto.resp.RatingResp;
import com.example.mountain.entity.RatingEntity;
import com.example.mountain.entity.RatingRecEntity;
import com.example.mountain.repository.RatingRecRepository;
import com.example.mountain.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingRecRepository ratingRecRepository;

    @Override
    public Flux<RatingResp> getRatingListPage(Long mountainId, int pageNum, int pageSize) {

        ArrayList<RatingResp> ratingResps = new ArrayList<>();
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "ratingNum");
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);

        Flux<RatingResp> result = ratingRepository.findByMountainId(mountainId, pageable)
                .publishOn(Schedulers.boundedElastic())
                .map(rating ->
                    new RatingResp(rating.getId(), rating.getStar(), rating.getComment(),
                            rating.getUserId(), rating.getTitle(), rating.getThumbImg(),
                            rating.getRatingNum(), rating.getRegdate(),
                            rating.getNickname(), rating.getMountainId()));

        return result;
    }

    @Override
    public Mono<RatingEntity> createRating(RatingEntity ratingEntity) {
        return ratingRepository.save(ratingEntity);
    }

    @Override
    public Mono<RatingEntity> updateRating(RatingRequest ratingRequest, Long ratingId) {
        return ratingRepository.findById(ratingId)
                .map(r -> {
                    RatingEntity ratingEntity = RatingEntity.builder()
                            .id(ratingId)
                            .title(ratingRequest.getTitle())
                            .comment(ratingRequest.getComment())
                            .thumbImg(ratingRequest.getThumbImg())
                            .star(ratingRequest.getStar())

                            .userId(r.getUserId())
                            .mountainId(r.getMountainId())
                            .ratingNum(r.getRatingNum())
                            .nickname(r.getNickname()).build();
                    return ratingEntity;
                })
                .flatMap(r -> ratingRepository.save(r));
    }

    @Override
    public Mono<Void> deleteRating(Long id) {
        return ratingRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Mono<Integer> toggleRecommend(Long ratingId, Long userId, Integer add) {

        if(add == 1){
            RatingRecEntity ratingRecEntity = new RatingRecEntity(userId, ratingId);
            ratingRecRepository.save(ratingRecEntity);
            ratingRepository.addRatingNum(ratingId);
        }else{
            ratingRecRepository.deleteByUserIdAndRatingId(userId, ratingId);
            ratingRepository.subRatingNum(ratingId);
        }
        return add == 1 ? ratingRepository.addRatingNum(ratingId) :  ratingRepository.subRatingNum(ratingId);
    }
}