package com.example.mountain.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(value="MOUNTAINS")
@Data
public class MountainEntity {
    @Id
    private Long id;

    @Column(value="name")
    private String name;

    @Column(value = "latitude")
    private Double latitude;

    @Column(value = "longitude")
    private Double longitude;

    @CreatedDate
    @Column(value = "regdate")
    private LocalDateTime regdate;

    @Column(value = "district_id")
    private Long districtId;

    private Integer height;

    @Column(value = "hiking_level")
    private Integer hikingLevel;

    @Column(value = "view_level")
    private Integer viewLevel;

    @Column(value = "attract_level")
    private Integer attractLevel;

    @Column(value="description")
    private String description;

    @Column(value = "short_description")
    private String shortDescription;

    // 객체와 테이블간에 연관관계를 맺는 차이를 이해해야함.
    // 객체의 양방향 관계는 서로 다른 단방향 관계 2개다.
    // 양방향 맵핑을 위한 작업
    // 하인
    @Transient
    private List<FavoriteEntity> favoriteEntityList = new ArrayList<>();

    @Transient
    private List<MountainThumbEntity> mountainThumbEntityList = new ArrayList<>();
}
