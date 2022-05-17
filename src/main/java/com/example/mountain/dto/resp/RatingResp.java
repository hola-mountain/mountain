package com.example.mountain.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingResp {

    private Long ratingId;
    private Integer star;
    private String comment;
    private Long userId;
    private String title;
    private String thumbImg;
    private Long recommendNum;
    private LocalDateTime regdate;
    private String nickname;

    private Long mountainId;

    public RatingResp() {
    }

    public RatingResp(Long ratingId){
        this.ratingId = ratingId;
    }

    public RatingResp(Long ratingId, Integer star, String comment, Long userId, String title, String thumbImg, Long recommendNum, LocalDateTime regdate, String nickname, Long mountainId) {
        this.ratingId = ratingId;
        this.star = star;
        this.comment = comment;
        this.userId = userId;
        this.title = title;
        this.thumbImg = thumbImg;
        this.recommendNum = recommendNum;
        this.regdate = regdate;
        this.nickname = nickname;
        this.mountainId = mountainId;
    }
}
