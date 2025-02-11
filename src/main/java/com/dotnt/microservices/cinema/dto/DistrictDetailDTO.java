package com.dotnt.microservices.cinema.dto;

import lombok.Data;

import java.util.List;

@Data
public class DistrictDetailDTO {
    private String name;
    private Integer code;
    private List<WardDTO> wards;
}
