package com.example.mountain.service;

import com.example.mountain.dto.kakfa.ReviewCount;
import com.example.mountain.dto.req.RatingRequest;
import com.example.mountain.dto.resp.DeleteRatingResp;
import com.example.mountain.dto.resp.FavoriteMountainResp;
import com.example.mountain.dto.resp.RatingMountainResp;
import com.example.mountain.dto.resp.RatingResp;
import com.example.mountain.entity.FavoriteEntity;
import com.example.mountain.entity.RatingEntity;
import com.example.mountain.entity.RatingRecEntity;
import com.example.mountain.exception.NoDataFounedException;
import com.example.mountain.repository.MountainRepository;
import com.example.mountain.repository.RatingRecRepository;
import com.example.mountain.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingRecRepository ratingRecRepository;
    private final MountainRepository mountainRepository;
    private final KafkaTemplate<String, ReviewCount> ratingKafkaTemplate;

    private static final String TOPIC = "mountainbadge";

    public void sendMessage(ReviewCount message) {
        log.info(String.format("#### -> Producing message -> %s", message.toString()));
        this.ratingKafkaTemplate.send(TOPIC ,message);
    }

    @Override
    public Flux<RatingResp> getRatingListPage(Long mountainId, int pageNum, int pageSize) {

        ArrayList<RatingResp> ratingResps = new ArrayList<>();
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "ratingNum", "regdate");
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

        Mono<RatingEntity> mono = ratingRepository.save(ratingEntity);

        return mono.doOnNext(x->
                sendMessage(ReviewCount.builder()
                        .userId(x.getUserId())
                        .ratingId(x.getId())
                        .add(1)
                        .build()));
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
    public Mono<Void> deleteRating(Long id, Long userId) {

        return ratingRepository.findById(id).hasElement()
                .flatMap(deleteRating ->  {
                    if (!deleteRating)
                        return Mono.error(new NoDataFounedException("해당 리뷰를 찾을수 없습니다."));

                    return deleteMountainReview(id, userId);
                }).log();
    }

    private Mono<Void> deleteMountainReview(Long ratingId, Long userId) {
        return ratingRepository.deleteById(ratingId)
                .doOnNext(x-> {
                    sendMessage(ReviewCount.builder()
                                    .userId(userId)
                                    .ratingId(ratingId)
                                    .add(-1)
                                    .build());
                        });
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

    @Override
    public Flux<RatingMountainResp> getMyPageReviewMountain(Long userId) {
        Flux<RatingEntity> flux = ratingRepository.findByUserId(userId);

        return flux
                .flatMap(x-> mountainRepository.findById(x.getMountainId())
                        .flatMap(y->
                                Mono.zip(Mono.just(y), Mono.just(x)))
                        .map(t -> {
                            //t1 -> mountain
                            //t2 -> rating
                            return RatingMountainResp.builder()
                                    .ratingId(t.getT2().getId())
                                    .star(t.getT2().getStar())
                                    .comment(t.getT2().getComment())
                                    .title(t.getT2().getTitle())
                                    .thumbImg(t.getT2().getThumbImg())
                                    .nickname(t.getT2().getNickname())
                                    .recommendNum(t.getT2().getRatingNum())
                                    .mountainId(t.getT1().getId())
                                    .name(t.getT1().getName())
                                    .build();
                        })).log();
    }
}
