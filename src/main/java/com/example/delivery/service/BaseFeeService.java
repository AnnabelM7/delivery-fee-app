package com.example.delivery.service;

import com.example.delivery.entity.BaseFee;
import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.exception.ResourceNotFoundException;
import com.example.delivery.repository.BaseFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BaseFeeService {

    private final BaseFeeRepository baseFeeRepository;

    public List<BaseFee> getAllBaseFees() {
        return baseFeeRepository.findAll();
    }

    public BaseFee getBaseFeeById(UUID id) {
        return baseFeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Base fee not found"));
    }

    public BaseFee createBaseFee(BaseFee baseFee) {
        return baseFeeRepository.save(baseFee);
    }

    public BaseFee updateBaseFee(UUID id, BaseFee updatedBaseFee) {
        BaseFee baseFee = getBaseFeeById(id);
        baseFee.setCity(updatedBaseFee.getCity());
        baseFee.setVehicleType(updatedBaseFee.getVehicleType());
        baseFee.setFee(updatedBaseFee.getFee());
        return baseFeeRepository.save(baseFee);
    }

    public void deleteBaseFee(UUID id) {
        baseFeeRepository.deleteById(id);
    }

    public BaseFee getBaseFeeByCityAndVehicleType(City city, VehicleType vehicleType) {
        return baseFeeRepository.findByCityAndVehicleType(city, vehicleType)
                .orElseThrow(() -> new ResourceNotFoundException("No base fee configured for city=" + city + ", vehicleType=" + vehicleType));
    }
}