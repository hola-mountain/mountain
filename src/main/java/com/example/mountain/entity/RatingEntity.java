package com.example.mountain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Table(value="RATINGS")
@Builder
public class RatingEntity {

    @Id
    private Long id;

    @Column(value="star")
    private Integer star;

    @Column(value="comment")
    private String comment;

    @CreatedDate
    @Column(value = "regdate")
    private LocalDateTime regdate;

    @Column(value="mountain_id")
    private Long mountainId;

    @Column(value="user_id")
    private Long userId;

    @Column(value="title")
    private String title;

    @Column(value="thumb_img")
    private String thumbImg;

    @Column(value="nickname")
    private String nickname;

    @Column(value="rating_num")
    private Long ratingNum;

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }
}
