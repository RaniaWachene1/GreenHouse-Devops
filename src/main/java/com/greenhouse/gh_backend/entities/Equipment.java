package com.greenhouse.gh_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Equipment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipment;
    private String equipmentType;
    private String equipmentName;
    private String equipmentRef;

    private int quantity;
    private double powerRating; // kW
    private double annualUsageHours; // hours per year
    private double co2Emissions; // tCO2e

    @Enumerated(EnumType.STRING)
    private EmissionScope scope = EmissionScope.SCOPE_3;

    @Enumerated(EnumType.STRING)
    private EmissionSource emissionSource = EmissionSource.EQUIPMENT;


    @ManyToOne
    @JsonIgnore
    private User user;
}
