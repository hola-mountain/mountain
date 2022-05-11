package com.example.mountain.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private ArrayList<String> images = new ArrayList<>();

    public MountainResp() {
    }

    public MountainResp(Long mountainId, String name, String shortDescription) {
        this.mountainId = mountainId;
        this.name = name;
        this.shortDescription = shortDescription;
    }

    public MountainResp(Long mountainId, String name, Double latitude, Double longitude, Integer height, Integer hikingLevel, Integer viewLevel, Integer attractLevel, String description, ArrayList<String> images) {
        this.mountainId = mountainId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.hikingLevel = hikingLevel;
        this.viewLevel = viewLevel;
        this.attractLevel = attractLevel;
        this.description = description;
        this.images = images;
    }
}
