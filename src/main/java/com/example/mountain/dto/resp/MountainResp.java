package com.example.mountain.dto.resp;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MountainResp {

    private Long mountainId;
    private String name;
    private String shortDescription;

    private Double latitude;
    private Double longitude;
    private Integer height;
    private Integer hikingLevel;
    private Integer viewLevel;
    private Integer attractLevel;
    private String description;
    private ArrayList<String> image = new ArrayList<>();
    private String img;

    public MountainResp() {
    }

    public MountainResp(Long mountainId, String name, String shortDescription) {
        this.mountainId = mountainId;
        this.name = name;
        this.shortDescription = shortDescription;
    }

    public MountainResp(Long mountainId, String name, String shortDescription, ArrayList<String> image) {
        this.mountainId = mountainId;
        this.name = name;
        this.shortDescription = shortDescription;
        this.image = image;
    }

    public MountainResp(Long mountainId, String name, Double latitude, Double longitude, Integer height, Integer hikingLevel, Integer viewLevel, Integer attractLevel, String description, ArrayList<String> image) {
        this.mountainId = mountainId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.hikingLevel = hikingLevel;
        this.viewLevel = viewLevel;
        this.attractLevel = attractLevel;
        this.description = description;
        this.image = image;
    }
}
