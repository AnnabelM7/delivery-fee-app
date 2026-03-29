package com.example.delivery.entity;

import com.example.delivery.enums.FeeType;
import com.example.delivery.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "extra_fees")
public class ExtraFee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeType feeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    private Double minValue;
    private Double maxValue;
    private String phenomenon;
    private Double fee;

    @Column(nullable = false)
    private boolean forbidden;
}