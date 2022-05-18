package com.example.mountain.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class RatingMountainResp {

    private Long ratingId;
    private Integer star;
    private String comment;
    private LocalDate regdate;
    private String title;
    private String thumbImg;
    private String nickname;
    private Long recommendNum;


    private Long mountainId;
    private String name;

}
