package com.greenhouse.gh_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocation;

    private String nameLocation;

    private String addressLocation;

    private String aptSuite;
    private String city;
    private String state;
    private int zip;
    private String country;
    private boolean usesNaturalGas;
    private double grossArea;
    private String sq;
    private String primaryUse;
    private double latitude;
    private double longitude;
    @ManyToOne
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Emission> emissions = new ArrayList<>();
    private double naturalGasConsumption;
    private double co2Emissions;

    private double electricityConsumption; // in kWh
    private double electricityCo2Emissions;
    public boolean getUsesNaturalGas() {
        return usesNaturalGas;
    }
    public void addEmission(Emission emission) {
        this.emissions.add(emission);
        emission.setLocation(this);
    }

    public void removeEmission(Emission emission) {
        this.emissions.remove(emission);
        emission.setLocation(null);
    }

}
