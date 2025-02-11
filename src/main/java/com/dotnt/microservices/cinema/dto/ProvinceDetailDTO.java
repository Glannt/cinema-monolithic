package com.dotnt.microservices.cinema.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceDetailDTO {
    private String name;
    private Integer code;
    private List<DistrictDTO> districts;
}
