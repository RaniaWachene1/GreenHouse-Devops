package com.greenhouse.gh_backend.restController;

import com.greenhouse.gh_backend.entities.Location;
import com.greenhouse.gh_backend.repositories.LocationRepository;
import com.greenhouse.gh_backend.services.EmissionCalculationService;
import com.greenhouse.gh_backend.services.ILocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"*", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")

//@CrossOrigin(origins = {"http://localhost:4200", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired
    private ILocation locationService;
    @Autowired
    private EmissionCalculationService emissionCalculationService;
    @Autowired
    private LocationRepository locationRepository;
    @GetMapping("/user/{idUser}")
    public List<Location> getLocationsByUserId(@PathVariable Long idUser) {
        return locationService.getLocationsByUserId(idUser);
    }
    @PutMapping("/update/{id}")
    public Location updateLocation(@RequestBody Location location, @PathVariable Long id) {
        return locationService.updateLocation(location, id);
    }

    @PostMapping("/add/{userId}")
    public Location createLocation(@RequestBody Location location, @PathVariable Long userId) {
        return locationService.saveLocation(location, userId);
    }
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }

    @GetMapping("/user/{idUser}/natural-gas")
    public List<Location> getLocationsByUserIdAndUsesNaturalGas(@PathVariable Long idUser) {
        return locationService.getLocationsByUserIdAndUsesNaturalGas(idUser);
    }
    @GetMapping("/total-co2/{userId}")
    public ResponseEntity<Double> getTotalCo2EmissionsForUser(@PathVariable long userId) {
        Double totalCo2Emissions = locationService.getTotalCo2EmissionsForUser(userId);
        return ResponseEntity.ok(totalCo2Emissions);
    }
}
