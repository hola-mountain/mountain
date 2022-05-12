package com.example.mountain.entity;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(value="FAVORITES")
@Data
public class FavoriteEntity {

    @Id
    private Long id;

    @Column(value = "mountain_id")
    private Long mountainId;

    @Column(value = "user_id")
    private Long userId;

    @CreatedDate
    @Column(value = "regdate")
    private LocalDateTime regdate;

    public FavoriteEntity(Long mountainId, Long userId) {
        this.mountainId = mountainId;
        this.userId = userId;
    }
}
