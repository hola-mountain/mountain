package com.example.mountain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class RatingRequest {

    private String comment;
    private Integer star;
    private String thumbImg;
    private String title;
    private Long userId;
    private String nickname;

    public RatingRequest() {
    }

    public RatingRequest(String comment, Integer star, String thumbImg, String title, Long userId) {
        this.comment = comment;
        this.star = star;
        this.thumbImg = thumbImg;
        this.title = title;
        this.userId = userId;
    }

    public RatingRequest(String comment, Integer star, String thumbImg, String title, Long userId, String nickname) {
        this.comment = comment;
        this.star = star;
        this.thumbImg = thumbImg;
        this.title = title;
        this.userId = userId;
        this.nickname = nickname;
    }
}
