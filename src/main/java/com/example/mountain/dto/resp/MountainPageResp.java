package com.example.mountain.dto.resp;

import lombok.*;

import java.util.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MountainPageResp {

    Long totalPageSize;
    List<MountainResp> mountainResp = new ArrayList<>();
}
