package com.example.mountain.repository;

import com.example.mountain.entity.MountainEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface MountainRepository extends ReactiveSortingRepository<MountainEntity,Long> {

    Flux<MountainEntity> findByDistrictId(int district, Pageable pageable);
}
