package com.example.mountain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class RatingRecEntity {
    @Id
    private Long id;
    private Long userId;
    private Long ratingId;

    public RatingRecEntity() {
    }

    public RatingRecEntity(Long userId, Long ratingId) {
        this.userId = userId;
        this.ratingId = ratingId;
    }
}
