package com.dotnt.microservices.cinema.configuration;

import com.dotnt.microservices.cinema.common.UserType;
import com.dotnt.microservices.cinema.dto.*;
import com.dotnt.microservices.cinema.model.Address;
import com.dotnt.microservices.cinema.model.Role;
import com.dotnt.microservices.cinema.repositories.AddressRepository;
import com.dotnt.microservices.cinema.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "INIT-APPLICATION")
public class InitApp {

    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final RestTemplate restTemplate;
//    @Value("${external.api.address.url}")
//    private String addressApiUrl;

    private static final String PROVINCE_API_URL = "https://provinces.open-api.vn/api/";
    private static final String DISTRICT_API_URL = "https://provinces.open-api.vn/api/p/{code}?depth=2";
    private static final String WARD_API_URL = "https://provinces.open-api.vn/api/d/{code}?depth=2";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner initApplication() {
        log.info("Initializing application.....");
        return args -> {
            Optional<Role> roleUser = roleRepository.findByName(String.valueOf(UserType.USER));
            if(roleUser.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.USER))
                        .description("User role")
                        .build());
            }

            Optional<Role> roleAdmin = roleRepository.findByName(String.valueOf(UserType.ADMIN));
            if(roleAdmin.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.ADMIN))
                        .description("Admin role")
                        .build());
            }

            Optional<Role> roleManager = roleRepository.findByName(String.valueOf(UserType.MANAGER));
            if(roleManager.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.MANAGER))
                        .description("Manager role")
                        .build());
            }

            Optional<Role> roleStaff = roleRepository.findByName(String.valueOf(UserType.STAFF));
            if(roleStaff.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.STAFF))
                        .description("Staff role")
                        .build());
            }
            log.info("Application initialization completed .....");
        };
    }

    @PostConstruct
    public void initializeAddresses() {
        try {
            if (addressRepository.count() == 0) {
                log.info("Starting Vietnam address initialization");
                List<ProvinceDTO> provinces = fetchProvinces();
                for (ProvinceDTO province : provinces) {
                    processProvince(province);
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize addresses: {}", e.getMessage());
        }
    }

    private List<ProvinceDTO> fetchProvinces() {
        ResponseEntity<ProvinceDTO[]> response = restTemplate.getForEntity(
                PROVINCE_API_URL,
                ProvinceDTO[].class
        );
        return Arrays.asList(response.getBody());
    }

    private void processProvince(ProvinceDTO province) {
        ResponseEntity<ProvinceDetailDTO> response = restTemplate.getForEntity(
                DISTRICT_API_URL,
                ProvinceDetailDTO.class,
                province.getCode()
        );

        ProvinceDetailDTO provinceDetail = response.getBody();
        if (provinceDetail != null && provinceDetail.getDistricts() != null) {
            for (DistrictDTO district : provinceDetail.getDistricts()) {
                processDistrict(province, district);
            }
        }
    }

    private void processDistrict(ProvinceDTO province, DistrictDTO district) {
        ResponseEntity<DistrictDetailDTO> response = restTemplate.getForEntity(
                WARD_API_URL,
                DistrictDetailDTO.class,
                district.getCode()
        );

        DistrictDetailDTO districtDetail = response.getBody();
        if (districtDetail != null && districtDetail.getWards() != null) {
            for (WardDTO ward : districtDetail.getWards()) {
                saveAddress(province, district, ward);
            }
        }
    }

    private void saveAddress(ProvinceDTO province, DistrictDTO district, WardDTO ward) {
        Address address = Address.builder()
                .country("Vietnam")
                .province(province.getName())
                .city(province.getName())
                .district(district.getName())
                .commune(ward.getName())
                .provinceCode(province.getCode())
                .districtCode(district.getCode())
                .wardCode(ward.getCode())
                .build();

        addressRepository.save(address);
    }

}