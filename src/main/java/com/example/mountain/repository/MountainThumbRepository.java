package com.example.mountain.repository;

import com.example.mountain.entity.MountainThumbEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MountainThumbRepository extends ReactiveCrudRepository<MountainThumbEntity,Long> {
    Flux<MountainThumbEntity> findByMountainId(Long mountainId);
}
