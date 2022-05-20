package com.example.mountain.service;

import com.example.mountain.dto.resp.FavoriteMountainResp;
import com.example.mountain.entity.FavoriteEntity;
import com.example.mountain.repository.FavoriteRepository;
import com.example.mountain.repository.MountainRepository;
import com.example.mountain.repository.MountainThumbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final MountainRepository mountainRepository;
    private final MountainThumbRepository mountainThumbRepository;

    @Override
    public Mono<FavoriteEntity> addFavorite(Long userId, Long mountainId) {
        return favoriteRepository.save(new FavoriteEntity(mountainId, userId));
    }

    @Override
    public Flux<Void> removeFavorite(Long userId, Long mountainId) {
        //System.out.println(userId + ": " +  mountainId);
        return favoriteRepository.findByUserIdAndMountainId(userId, mountainId)
                .flatMap(r-> favoriteRepository.deleteById(r.getId())
                        .doOnNext(res -> log.info("Item  has been removed")));
    }

    @Override
    public Flux<FavoriteMountainResp> getMyPageFavoriteMountain(Long userId) {

        Flux<FavoriteEntity> flux = favoriteRepository.findByUserId(userId);

        return flux
                .flatMap(x-> mountainRepository.findById(x.getMountainId())
                .flatMap(y->
                    Mono.zip(Mono.just(y), mountainThumbRepository.findByMountainId(y.getId()).collectList(), Mono.just(x)))
                        .map(t -> {
                ArrayList<String> i = new ArrayList();
                t.getT2().forEach(img-> i.add(img.getImage()));
                            // t1 : mountain
                            // t2 : mountain_thum
                            // t3 : favorite
                // mountainId,  name,  shortDescription, > image,  favoriteId, LocalDate regdate
                return new FavoriteMountainResp(t.getT1().getId(), t.getT1().getName(), t.getT1().getShortDescription(), i, x.getId());
                })).log();
    }

    @Override
    public Mono<FavoriteMountainResp> getMountainDetailFavorite(Long mountainId, Long userId) {
        return favoriteRepository.findByMountainIdAndUserId(mountainId, userId)
                .map(x-> new FavoriteMountainResp(1)).defaultIfEmpty(new FavoriteMountainResp(0));
    }
}
