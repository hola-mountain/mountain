package com.example.mountain.service;

import com.example.mountain.dto.resp.MountainResp;
import com.example.mountain.entity.MountainThumbEntity;
import com.example.mountain.repository.MountainRepository;
import com.example.mountain.repository.MountainThumbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService{

    private final MountainRepository mountainRepository;
    private final MountainThumbRepository mountainThumbRepository;

    @Override
    public Flux<MountainResp> getMountainListPage(int district, int pageNum, int pageSize, String sortBy, boolean isAsc, String search) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);

        if(district == 0){
            return mountainRepository.findByNameLike("%"+search+"%", pageable)
                    .flatMap(x->
                            Mono.zip(Mono.just(x), mountainThumbRepository.findByMountainId(x.getId()).collectList())
                    ).map(t ->
                    {
                        ArrayList<String> i = new ArrayList();
                        t.getT2().forEach(x-> i.add(x.getImage()));
                        return new MountainResp(t.getT1().getId(), t.getT1().getName(), t.getT1().getShortDescription(), i);
                    });
        }
            return mountainRepository.findByDistrictIdAndNameLike(district , "%"+search+"%", pageable)
                    .flatMap(x->
                            Mono.zip(Mono.just(x), mountainThumbRepository.findByMountainId(x.getId()).collectList())
                    ).map(t ->
                    {
                        ArrayList<String> i = new ArrayList();
                        t.getT2().forEach(x-> i.add(x.getImage()));
                        return new MountainResp(t.getT1().getId(), t.getT1().getName(), t.getT1().getShortDescription(), i);
                    });
    }

    @Override
    public Mono<MountainResp> getMountainDetail(Long mountainId) {
        ArrayList<String> img = new ArrayList<String>();
        return Mono.zip(
                mountainRepository.findById(mountainId),
                mountainThumbRepository.findByMountainId(mountainId).collectList(),
                (x,y) -> {
                    for(MountainThumbEntity m : y){
                        img.add(m.getImage());
                    }
                    return new MountainResp(x.getId(),x.getName(),x.getLatitude(),x.getLongitude(),x.getHeight(),x.getHikingLevel(),x.getViewLevel(),x.getAttractLevel(),x.getDescription(),img);
                }
        ).log();
    }
}
