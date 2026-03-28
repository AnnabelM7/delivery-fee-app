package com.example.delivery.service;

import com.example.delivery.entity.BaseFee;
import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.exception.ResourceNotFoundException;
import com.example.delivery.repository.BaseFeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseFeeServiceTest {

    @Mock
    private BaseFeeRepository baseFeeRepository;

    @InjectMocks
    private BaseFeeService baseFeeService;

    private BaseFee tartuBike;

    @BeforeEach
    void setUp() {
        tartuBike = new BaseFee();
        tartuBike.setId(UUID.randomUUID());
        tartuBike.setCity(City.TARTU);
        tartuBike.setVehicleType(VehicleType.BIKE);
        tartuBike.setFee(2.5);
    }

    @Test
    void getAllBaseFees_returnsAll() {
        when(baseFeeRepository.findAll()).thenReturn(List.of(tartuBike));

        List<BaseFee> result = baseFeeService.getAllBaseFees();

        assertThat(result).hasSize(1);
    }

    @Test
    void getBaseFeeById_found_returnsBaseFee() {
        when(baseFeeRepository.findById(tartuBike.getId())).thenReturn(Optional.of(tartuBike));

        BaseFee result = baseFeeService.getBaseFeeById(tartuBike.getId());

        assertThat(result.getFee()).isEqualTo(2.5);
    }

    @Test
    void getBaseFeeById_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(baseFeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> baseFeeService.getBaseFeeById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getBaseFeeByCityAndVehicleType_found_returnsBaseFee() {
        when(baseFeeRepository.findByCityAndVehicleType(City.TARTU, VehicleType.BIKE))
                .thenReturn(Optional.of(tartuBike));

        BaseFee result = baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE);

        assertThat(result.getFee()).isEqualTo(2.5);
    }

    @Test
    void getBaseFeeByCityAndVehicleType_notFound_throwsException() {
        when(baseFeeRepository.findByCityAndVehicleType(City.TARTU, VehicleType.BIKE))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createBaseFee_savesAndReturns() {
        when(baseFeeRepository.save(tartuBike)).thenReturn(tartuBike);

        BaseFee result = baseFeeService.createBaseFee(tartuBike);

        assertThat(result.getFee()).isEqualTo(2.5);
        verify(baseFeeRepository, times(1)).save(tartuBike);
    }

    @Test
    void deleteBaseFee_callsRepository() {
        baseFeeService.deleteBaseFee(tartuBike.getId());

        verify(baseFeeRepository, times(1)).deleteById(tartuBike.getId());
    }
}