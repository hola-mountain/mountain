package com.example.mountain.service;

import com.example.mountain.dto.resp.MountainPageResp;
import com.example.mountain.dto.resp.MountainResp;
import com.example.mountain.entity.MountainThumbEntity;
import com.example.mountain.repository.FavoriteRepository;
import com.example.mountain.repository.MountainRepository;
import com.example.mountain.repository.MountainThumbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService{

    private final MountainRepository mountainRepository;
    private final MountainThumbRepository mountainThumbRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public Mono<MountainPageResp> getMountainListPage(int district, int pageNum, int pageSize, String sortBy, boolean isAsc, String search) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);
        MountainPageResp mountainPageResp = new MountainPageResp();

        if(district == 0){
            mountainRepository.findSize(district, search).doOnNext(x-> mountainPageResp.setTotalPageSize(x.longValue())).subscribe();
            mountainRepository.findByNameLike(search, pageable)
                    .flatMap(x->
                            Mono.zip(Mono.just(x), mountainThumbRepository.findByMountainId(x.getId()).collectList())
                    ).map(t ->
                    {
                        ArrayList<String> i = new ArrayList();
                        t.getT2().forEach(x-> i.add(x.getImage()));
                        return mountainPageResp.getMountainResp().add(new MountainResp(t.getT1().getId(), t.getT1().getName(), t.getT1().getShortDescription(), i));
                    }).log().subscribe();
        }
        else {
        mountainRepository.findSize(district, search).doOnNext(x-> mountainPageResp.setTotalPageSize(x.longValue())).subscribe();

        mountainRepository.findByDistrictIdAndNameLike(district , search, pageable)
                .flatMap(x->
                        Mono.zip(Mono.just(x), mountainThumbRepository.findByMountainId(x.getId()).collectList())
                ).map(t ->
                {
                    ArrayList<String> i = new ArrayList();
                    t.getT2().forEach(x-> i.add(x.getImage()));
                    return mountainPageResp.getMountainResp().add(new MountainResp(t.getT1().getId(), t.getT1().getName(), t.getT1().getShortDescription(), i));
                })
                .subscribe();
        }
        return Mono.just(mountainPageResp).delayElement(Duration.ofSeconds(1));
    }

    @Override
    public Mono<MountainResp> getMountainDetail(Long mountainId, Long userId) {
        ArrayList<String> img = new ArrayList<String>();
        return Mono.zip(
                mountainRepository.findById(mountainId),
                mountainThumbRepository.findByMountainId(mountainId).collectList(),
                favoriteRepository.findByMountainIdAndUserId(mountainId,userId)
                ).map(t-> {
                    for(MountainThumbEntity m : t.getT2()){
                        img.add(m.getImage());
                    }

                    int fav = t.getT3() == null ? 0 : 1;
                    return new MountainResp(t.getT1().getId(),t.getT1().getName(),t.getT1().getLatitude(),t.getT1().getLongitude(),t.getT1().getHeight(),t.getT1().getHikingLevel(),
                            t.getT1().getViewLevel(),t.getT1().getAttractLevel(),t.getT1().getDescription(),img, fav);
                });
    }
}
