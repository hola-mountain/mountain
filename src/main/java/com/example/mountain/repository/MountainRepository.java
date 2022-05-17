package com.example.mountain.repository;

import com.example.mountain.entity.MountainEntity;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface MountainRepository extends ReactiveSortingRepository<MountainEntity,Long> {


    //@Query(value = "SELECT id, name, short_description FROM MOUNTAINS  WHERE district_id =:district AND name LIKE :name")
    Flux<MountainEntity> findByDistrictIdAndNameLike(int district, String name, Pageable pageable);

    Flux<MountainEntity> findByDistrictId(int district, Pageable pageable);

    Flux<MountainEntity> findAllBy(@NonNull Pageable pageable);
}

