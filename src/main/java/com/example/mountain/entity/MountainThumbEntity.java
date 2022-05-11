package com.example.mountain.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Table(value="MOUNTAIN_THUMBS")
@Data
public class MountainThumbEntity {

    @Id
    private Long id;

    @CreatedDate
    @Column(value = "regdate")
    private LocalDateTime regdate;

    private String image;
    private Long mountainId;

    @Transient
    private MountainEntity mountainEntity;


}
