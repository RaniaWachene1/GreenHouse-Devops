package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Vehicle;

import java.util.List;

public interface IVehicle {
    List<Vehicle> getVehiclesByUserId(Long userId);
    Vehicle saveVehicle(Vehicle vehicle, Long userId);
    void deleteVehicle(Long id);
    Vehicle updateVehicle(Vehicle vehicle, Long id);
}
