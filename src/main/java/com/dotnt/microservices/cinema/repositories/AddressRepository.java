package com.dotnt.microservices.cinema.repositories;

import com.dotnt.microservices.cinema.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT a.id FROM Address a WHERE a.provinceCode = :pCode AND a.districtCode = :dCode AND a.wardCode = :wCode")
    UUID findIdByProvinceCodeAndDistrictCodeAndWardCode(@Param("pCode") String pCode,
                                                        @Param("dCode") String dCode,
                                                        @Param("wCode") String wCode);
}
