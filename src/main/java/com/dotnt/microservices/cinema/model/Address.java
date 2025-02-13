package com.dotnt.microservices.cinema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "Address")
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends AbstractEntity<UUID> {


    private String country;


    private String province;


    private String city;


    private String district;


    private String commune;


    private String village;

    private Integer provinceCode;
    private Integer districtCode;
    private Integer wardCode;
}
