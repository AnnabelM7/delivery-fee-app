package com.example.delivery.repository;

import com.example.delivery.entity.BaseFee;
import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BaseFeeRepository extends JpaRepository<BaseFee, UUID> {
    Optional<BaseFee> findByCityAndVehicleType(City city, VehicleType vehicleType);
}
