package com.example.mountain.dto.kakfa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCount {
    Long userId;
    Long ratingId;
    Integer add;
}
