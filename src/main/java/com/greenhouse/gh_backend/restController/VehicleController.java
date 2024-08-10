package com.greenhouse.gh_backend.restController;

import com.greenhouse.gh_backend.entities.Vehicle;
import com.greenhouse.gh_backend.services.IVehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = {"http://localhost:4200", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private IVehicle vehicleService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle, @PathVariable Long userId) {
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicle, userId);
        return ResponseEntity.ok(savedVehicle);
    }


    @GetMapping("/user/{idUser}")
    public List<Vehicle> getVehiclesByUserId(@PathVariable Long idUser) {
        return vehicleService.getVehiclesByUserId(idUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Vehicle>> getAllVehicles(@RequestParam Long userId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle vehicle, @PathVariable Long id) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicle, id);
        return ResponseEntity.ok(updatedVehicle);
    }


}
