package com.example.mountain.repository;

import com.example.mountain.entity.MountainThumbEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MountainThumbRepository extends ReactiveCrudRepository<MountainThumbEntity,Long> {
    Flux<MountainThumbEntity> findByMountainId(Long mountainId);
}
