package com.example.mountain.service;

import com.example.mountain.dto.resp.MountainResp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MountainService {
    Flux<MountainResp> getMountainListPage(int district, int pageNum, int pageSize, String sortBy, boolean isAsc, String search);
    Flux<MountainResp> getMountainListPage(int pageNum, int pageSize, String sortBy, boolean isAsc);

    Mono<MountainResp> getMountainDetail(Long mountainId);
}
