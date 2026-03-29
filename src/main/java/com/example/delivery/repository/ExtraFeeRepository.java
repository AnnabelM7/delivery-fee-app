package com.example.delivery.repository;

import com.example.delivery.entity.ExtraFee;
import com.example.delivery.enums.FeeType;
import com.example.delivery.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExtraFeeRepository extends JpaRepository<ExtraFee, UUID> {

    /**
     * Returns all extra fee rules for a given fee type and vehicle type.
     *
     * @param feeType     the type of extra fee
     * @param vehicleType the vehicle type
     * @return list of matching rules
     */
    List<ExtraFee> findByFeeTypeAndVehicleType(FeeType feeType, VehicleType vehicleType);
}