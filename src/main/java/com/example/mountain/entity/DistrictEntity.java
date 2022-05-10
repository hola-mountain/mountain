package com.example.mountain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table(value="DISTRICTS")
@Data
public class DistrictEntity {

    @Id
    private Long id;

    private String area;
}
