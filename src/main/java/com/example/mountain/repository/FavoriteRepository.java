package com.example.mountain.repository;

import com.example.mountain.entity.FavoriteEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface FavoriteRepository extends ReactiveSortingRepository<FavoriteEntity,Long> {
}
