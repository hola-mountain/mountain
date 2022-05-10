package com.example.mountain.entity;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Table(value="FAVORITES")
@Data
public class FavoriteEntity {

    @Id
    private Long id;

    @Transient
    private MountainEntity mountainEntity;

    @Column(value = "user_id")
    private Long userId;

    @CreatedDate
    @Column(value = "regdate")
    private LocalDateTime regdate;
}
