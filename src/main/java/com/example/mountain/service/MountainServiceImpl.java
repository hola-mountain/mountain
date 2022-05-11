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
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService{

    private final MountainRepository mountainRepository;
    private final MountainThumbRepository mountainThumbRepository;

    @Override
    public Flux<MountainResp> getMountainListPage(int district, int pageNum, int pageSize, String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);
        return mountainRepository.findByDistrictId(district, pageable)
            .publishOn(Schedulers.boundedElastic())
            .map(x-> new MountainResp(x.getId(), x.getName(), x.getShortDescription()));
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
