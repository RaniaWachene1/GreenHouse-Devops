package com.greenhouse.gh_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehicle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVehicle;

    @NotBlank(message = "Vehicle name is required")
    @Size(max = 100, message = "Vehicle name must be less than 100 characters")
    private String name;
    private Integer year;
    private String make;
    private String model;
    private String vanType;
    private String motorbikeType;
    private String hgvType;
    private String carType;
    private String usageData;

    @Size(max = 50, message = "Fuel type must be less than 50 characters")
    private String fuelType;

    private double fuelConsumption; // in liters or gallons

    private double co2Emissions;

    @Enumerated(EnumType.STRING)
    private EmissionScope scope = EmissionScope.SCOPE_1;

    @Enumerated(EnumType.STRING)
    private EmissionSource emissionSource = EmissionSource.VEHICLE;

    @ManyToOne
    @JsonIgnore
    private User user;
}
