package com.greenhouse.gh_backend.restController;

import com.greenhouse.gh_backend.entities.*;
import com.greenhouse.gh_backend.repositories.EmissionRepository;
import com.greenhouse.gh_backend.repositories.EquipmentRepository;
import com.greenhouse.gh_backend.repositories.LocationRepository;
import com.greenhouse.gh_backend.repositories.VehicleRepository;
import com.greenhouse.gh_backend.services.EmissionCalculationService;
import com.greenhouse.gh_backend.services.FootprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/emissions")
@CrossOrigin(origins = {"*", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")

//@CrossOrigin(origins = {"http://localhost:4200", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")
public class EmissionController {

    @Autowired
    private EmissionCalculationService emissionCalculationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EmissionRepository emissionRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;


    @PostMapping("/{id}/calculate")
    public double calculateEmissionsCO2FromNaturalGas(@PathVariable Long id, @RequestParam double naturalGasConsumption) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        double co2eEmissions = emissionCalculationService.calculateCO2FromNaturalGas(naturalGasConsumption);

        location.setNaturalGasConsumption(naturalGasConsumption);
        location.setCo2Emissions(co2eEmissions);

        locationRepository.save(location);
        Emission emission = new Emission();
        emission.setScope(EmissionScope.SCOPE_1);
        emission.setCo2Emissions(co2eEmissions);
        emission.setEmissionSource(EmissionSource.NATURAL_GAS);
        emission.setLocation(location);

        emissionRepository.save(emission);

        return co2eEmissions;
    }
    @PostMapping("/{id}/calculate-electricity")
    public double calculateEmissionsCO2FromElectricity(@PathVariable Long id, @RequestParam double electricityConsumption) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        double co2eEmissions = emissionCalculationService.calculateCO2FromElectricity(electricityConsumption);

        location.setElectricityConsumption(electricityConsumption);
        location.setElectricityCo2Emissions(co2eEmissions);

        locationRepository.save(location);
        Emission emission = new Emission();
        emission.setScope(EmissionScope.SCOPE_2);
        emission.setCo2Emissions(co2eEmissions);
        emission.setEmissionSource(EmissionSource.ELECTRICITY);
        emission.setLocation(location);

        emissionRepository.save(emission);

        return co2eEmissions;
    }
    @PostMapping("/vehicle/{id}/calculate")
    public double calculateCO2FromVehicle(@PathVariable Long id, @RequestParam double fuelConsumption, @RequestParam String fuelType) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        double co2eEmissions = emissionCalculationService.calculateCO2FromVehicle(fuelConsumption, fuelType);

        vehicle.setFuelConsumption(fuelConsumption);
        vehicle.setCo2Emissions(co2eEmissions);
        vehicle.setFuelType(fuelType);

        vehicleRepository.save(vehicle);

        Emission emission = new Emission();
        emission.setScope(EmissionScope.SCOPE_1);
        emission.setCo2Emissions(co2eEmissions);
        emission.setEmissionSource(EmissionSource.VEHICLE);
        emission.setVehicle(vehicle);

        emissionRepository.save(emission);

        return co2eEmissions;
    }


    @PostMapping("/equipment/{id}/calculate")
    public double calculateCO2FromEquipment(@PathVariable Long id, @RequestBody Equipment equipment) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        existingEquipment.setPowerRating(equipment.getPowerRating());
        existingEquipment.setAnnualUsageHours(equipment.getAnnualUsageHours());

        double co2eEmissions = emissionCalculationService.calculateCO2FromEquipment(existingEquipment);

        existingEquipment.setCo2Emissions(co2eEmissions);
        equipmentRepository.save(existingEquipment);

        Emission emission = new Emission();
        emission.setScope(EmissionScope.SCOPE_3);
        emission.setCo2Emissions(co2eEmissions);
        emission.setEmissionSource(EmissionSource.EQUIPMENT);
        emission.setEquipment(existingEquipment);

        emissionRepository.save(emission);

        return co2eEmissions;
    }

    @GetMapping("/total-footprint/{userId}")
    public ResponseEntity<Map<String, Double>> getTotalFootprintForUser(@PathVariable long userId) {
        Map<String, Double> totalFootprint = emissionCalculationService.getTotalFootprintForUser(userId);
        return ResponseEntity.ok(totalFootprint);
    }
    @GetMapping("/total-co2-emissions/scope1/{userId}")
    public ResponseEntity<Double> getTotalCo2EmissionsForUserAndScope1(@PathVariable long userId) {
        Double totalCo2Emissions = emissionCalculationService.getTotalCo2EmissionsForUserAndScope1(userId);
        return ResponseEntity.ok(totalCo2Emissions);
    }

    @GetMapping("/total-co2-emissions/scope2/{userId}")
    public ResponseEntity<Double> getTotalCo2EmissionsForUserAndScope2(@PathVariable long userId) {
        Double totalCo2Emissions = emissionCalculationService.getTotalCo2EmissionsForUserAndScope2(userId);
        return ResponseEntity.ok(totalCo2Emissions);
    }

    @GetMapping("/total-co2-emissions/scope3/{userId}")
    public ResponseEntity<Double> getTotalCo2EmissionsForUserAndScope3(@PathVariable long userId) {
        System.out.println("Fetching total CO2 emissions for Scope 3 for user: " + userId);
        Double totalCo2Emissions = emissionCalculationService.getTotalCo2EmissionsForUserAndScope3(userId);
        System.out.println("Total CO2 emissions for Scope 3: " + totalCo2Emissions);
        return ResponseEntity.ok(totalCo2Emissions);
    }
}
