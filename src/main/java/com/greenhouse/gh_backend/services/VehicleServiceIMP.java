package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.entities.Vehicle;
import com.greenhouse.gh_backend.repositories.UserRepository;
import com.greenhouse.gh_backend.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceIMP implements IVehicle {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Vehicle> getVehiclesByUserId(Long userId) {
        return vehicleRepository.findByUserIdUser(userId);
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            vehicle.setUser(user);
            return vehicleRepository.save(vehicle);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public Vehicle updateVehicle(Vehicle vehicle, Long id) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + id));

        if (vehicle.getName() != null) {
            existingVehicle.setName(vehicle.getName());
        }
        if (vehicle.getYear() != null) {
            existingVehicle.setYear(vehicle.getYear());
        }
        if (vehicle.getMake() != null) {
            existingVehicle.setMake(vehicle.getMake());
        }
        if (vehicle.getModel() != null) {
            existingVehicle.setModel(vehicle.getModel());
        }
        if (vehicle.getVanType() != null) {
            existingVehicle.setVanType(vehicle.getVanType());
        }
        if (vehicle.getMotorbikeType() != null) {
            existingVehicle.setMotorbikeType(vehicle.getMotorbikeType());
        }
        if (vehicle.getHgvType() != null) {
            existingVehicle.setHgvType(vehicle.getHgvType());
        }
        if (vehicle.getCarType() != null) {
            existingVehicle.setCarType(vehicle.getCarType());
        }
        if (vehicle.getUsageData() != null) {
            existingVehicle.setUsageData(vehicle.getUsageData());
        }
        if (vehicle.getFuelType() != null) {
            existingVehicle.setFuelType(vehicle.getFuelType());
        }
        if (vehicle.getFuelConsumption() != 0) {
            existingVehicle.setFuelConsumption(vehicle.getFuelConsumption());
        }
        if (vehicle.getCo2Emissions() != 0) {
            existingVehicle.setCo2Emissions(vehicle.getCo2Emissions());
        }
        if (vehicle.getScope() != null) {
            existingVehicle.setScope(vehicle.getScope());
        }
        if (vehicle.getEmissionSource() != null) {
            existingVehicle.setEmissionSource(vehicle.getEmissionSource());
        }

        return vehicleRepository.save(existingVehicle);
    }


}
