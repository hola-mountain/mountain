package com.example.mountain.dto.resp;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class FavoriteMountainResp {

    private Long mountainId;
    private String name;
    private String shortDescription;

    private ArrayList<String> image = new ArrayList<>();
    private Long favoriteId;

    private Integer favorite;

    public FavoriteMountainResp(Long mountainId, String name, String shortDescription, ArrayList<String> image, Long favoriteId) {
        this.mountainId = mountainId;
        this.name = name;
        this.shortDescription = shortDescription;
        this.image = image;
        this.favoriteId = favoriteId;
    }

    public FavoriteMountainResp(int favorite){
        this.favorite = favorite;
    }
}
