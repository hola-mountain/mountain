package com.example.mountain.service;

import com.example.mountain.dto.resp.MountainPageResp;
import com.example.mountain.dto.resp.MountainResp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MountainService {
    Mono<MountainPageResp> getMountainListPage(int district, int pageNum, int pageSize, String sortBy, boolean isAsc, String search);
    Mono<MountainResp> getMountainDetail(Long mountainId, Long userId);
}
