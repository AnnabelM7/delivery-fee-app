package com.example.delivery.service;

import com.example.delivery.entity.ExtraFee;
import com.example.delivery.enums.FeeType;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.exception.ForbiddenVehicleException;
import com.example.delivery.exception.ResourceNotFoundException;
import com.example.delivery.repository.ExtraFeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtraFeeService {

    private final ExtraFeeRepository extraFeeRepository;

    /**
     * Returns all extra fee rules.
     */
    public List<ExtraFee> getAllRules() {
        return extraFeeRepository.findAll();
    }

    /**
     * Returns an extra fee rule by ID.
     *
     * @param id the rule ID
     * @return the matching ExtraFeeRule
     * @throws ResourceNotFoundException if not found
     */
    public ExtraFee getRuleById(UUID id) {
        return extraFeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Extra fee rule not found with id=" + id));
    }

    /**
     * Creates a new extra fee rule.
     *
     * @param rule the rule to create
     * @return the saved rule
     */
    public ExtraFee createRule(ExtraFee rule) {
        return extraFeeRepository.save(rule);
    }

    /**
     * Updates an existing extra fee rule.
     *
     * @param id          the ID of the rule to update
     * @param updatedRule the new values
     * @return the updated rule
     * @throws ResourceNotFoundException if not found
     */
    public ExtraFee updateRule(UUID id, ExtraFee updatedRule) {
        ExtraFee rule = getRuleById(id);
        rule.setFeeType(updatedRule.getFeeType());
        rule.setVehicleType(updatedRule.getVehicleType());
        rule.setMinValue(updatedRule.getMinValue());
        rule.setMaxValue(updatedRule.getMaxValue());
        rule.setPhenomenon(updatedRule.getPhenomenon());
        rule.setFee(updatedRule.getFee());
        rule.setForbidden(updatedRule.isForbidden());
        return extraFeeRepository.save(rule);
    }

    /**
     * Deletes an extra fee rule by ID.
     *
     * @param id the ID of the rule to delete
     */
    public void deleteRule(UUID id) {
        extraFeeRepository.deleteById(id);
    }

    /**
     * Calculates air temperature extra fee based on DB rules.
     *
     * @param temperature the air temperature
     * @param vehicleType the vehicle type
     * @return extra fee in euros
     */
    public double calculateAirTemperatureExtraFee(Double temperature, VehicleType vehicleType) {
        if (temperature == null) return 0.0;

        return extraFeeRepository.findByFeeTypeAndVehicleType(FeeType.ATEF, vehicleType)
                .stream()
                .filter(rule -> matchesTemperature(rule, temperature))
                .findFirst()
                .map(rule -> {
                    if (rule.isForbidden()) throw new ForbiddenVehicleException();
                    return rule.getFee() != null ? rule.getFee() : 0.0;
                })
                .orElse(0.0);
    }

    /**
     * Calculates wind speed extra fee based on DB rules.
     *
     * @param windSpeed   the wind speed
     * @param vehicleType the vehicle type
     * @return extra fee in euros
     */
    public double calculateWindSpeedExtraFee(Double windSpeed, VehicleType vehicleType) {
        if (windSpeed == null) return 0.0;

        return extraFeeRepository.findByFeeTypeAndVehicleType(FeeType.WSEF, vehicleType)
                .stream()
                .filter(rule -> matchesWindSpeed(rule, windSpeed))
                .findFirst()
                .map(rule -> {
                    if (rule.isForbidden()) throw new ForbiddenVehicleException();
                    return rule.getFee() != null ? rule.getFee() : 0.0;
                })
                .orElse(0.0);
    }

    /**
     * Calculates weather phenomenon extra fee based on DB rules.
     *
     * @param phenomenon  the weather phenomenon
     * @param vehicleType the vehicle type
     * @return extra fee in euros
     */
    public double calculateWeatherPhenomenonExtraFee(String phenomenon, VehicleType vehicleType) {
        if (phenomenon == null || phenomenon.isBlank()) return 0.0;

        String p = phenomenon.toLowerCase();

        return extraFeeRepository.findByFeeTypeAndVehicleType(FeeType.WPEF, vehicleType)
                .stream()
                .filter(rule -> rule.getPhenomenon() != null && p.contains(rule.getPhenomenon()))
                .findFirst()
                .map(rule -> {
                    if (rule.isForbidden()) throw new ForbiddenVehicleException();
                    return rule.getFee() != null ? rule.getFee() : 0.0;
                })
                .orElse(0.0);
    }

    private boolean matchesTemperature(ExtraFee rule, Double temperature) {
        boolean aboveMin = rule.getMinValue() == null || temperature > rule.getMinValue();
        boolean belowMax = rule.getMaxValue() == null || temperature <= rule.getMaxValue();
        return aboveMin && belowMax;
    }

    private boolean matchesWindSpeed(ExtraFee rule, Double windSpeed) {
        boolean aboveMin = rule.getMinValue() == null || windSpeed > rule.getMinValue();
        boolean belowMax = rule.getMaxValue() == null || windSpeed <= rule.getMaxValue();
        return aboveMin && belowMax;
    }
}