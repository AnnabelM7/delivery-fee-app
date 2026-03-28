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

    /**
     * Returns all base fees.
     */
    public List<BaseFee> getAllBaseFees() {
        return baseFeeRepository.findAll();
    }


    /**
     * Returns a base fee by ID.
     *
     * @param id the base fee ID
     * @return the matching BaseFee entry
     * @throws ResourceNotFoundException if no base fee found with given ID
     */
    public BaseFee getBaseFeeById(UUID id) {
        return baseFeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Base fee not found with id=" + id));
    }

    /**
     * Creates a new base fee entry.
     *
     * @param baseFee the base fee to create
     * @return the saved BaseFee entry
     */
    public BaseFee createBaseFee(BaseFee baseFee) {
        return baseFeeRepository.save(baseFee);
    }

    /**
     * Updates an existing base fee entry.
     *
     * @param id             the ID of the base fee to update
     * @param updatedBaseFee the new values
     * @return the updated BaseFee entry
     * @throws ResourceNotFoundException if no base fee found with given ID
     */
    public BaseFee updateBaseFee(UUID id, BaseFee updatedBaseFee) {
        BaseFee baseFee = getBaseFeeById(id);
        baseFee.setCity(updatedBaseFee.getCity());
        baseFee.setVehicleType(updatedBaseFee.getVehicleType());
        baseFee.setFee(updatedBaseFee.getFee());
        return baseFeeRepository.save(baseFee);
    }


    /**
     * Deletes a base fee entry by ID.
     *
     * @param id the ID of the base fee to delete
     */
    public void deleteBaseFee(UUID id) {
        baseFeeRepository.deleteById(id);
    }


    /**
     * Returns the base fee for a given city and vehicle type.
     *
     * @param city        the delivery city
     * @param vehicleType the vehicle type
     * @return the matching BaseFee entry
     * @throws ResourceNotFoundException if no base fee configured for given city and vehicle type
     */
    public BaseFee getBaseFeeByCityAndVehicleType(City city, VehicleType vehicleType) {
        return baseFeeRepository.findByCityAndVehicleType(city, vehicleType)
                .orElseThrow(() -> new ResourceNotFoundException("No base fee configured for city=" + city + ", vehicleType=" + vehicleType));
    }
}