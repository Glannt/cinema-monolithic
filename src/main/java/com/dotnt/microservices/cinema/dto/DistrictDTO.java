package com.dotnt.microservices.cinema.dto;

import lombok.Data;

@Data
public class DistrictDTO {
    private String name;
    private Integer code;
    private String division_type;
    private String codename;
}
