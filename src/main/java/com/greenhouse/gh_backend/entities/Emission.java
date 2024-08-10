package com.greenhouse.gh_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Emission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmission;

    @Enumerated(EnumType.STRING)
    private EmissionScope scope;

    private double co2Emissions;
    @Enumerated(EnumType.STRING)
    private EmissionSource emissionSource;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

}
