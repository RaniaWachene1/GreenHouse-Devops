package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Emission;
import com.greenhouse.gh_backend.entities.Location;
import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.EmissionRepository;
import com.greenhouse.gh_backend.repositories.LocationRepository;
import com.greenhouse.gh_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceIMP implements ILocation {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmissionRepository emissionRepository;

    @Override
    public List<Location> getLocationsByUserId(Long idUser) {
        return locationRepository.findByUserIdUser(idUser);
    }

    @Override
    public List<Location> getLocationsByUserIdAndUsesNaturalGas(Long idUser) {
        return locationRepository.findByUserIdUserAndUsesNaturalGas(idUser, true);
    }

    @Override
    public Double getTotalCo2EmissionsForUser(long userId) {
        return locationRepository.findTotalCo2EmissionsByUserId(userId);
    }

    @Override
    public Location saveLocation(Location location, Long idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            location.setUser(user);
            if (location.getEmissions() == null) {
                location.setEmissions(new ArrayList<>());
            }
            return locationRepository.save(location);
        } else {
            throw new RuntimeException("User not found with ID: " + idUser);
        }
    }

    @Override
    public Location updateLocation(Location location, Long id) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with ID: " + id));

        // Update fields as necessary
        if (location.getNameLocation() != null) {
            existingLocation.setNameLocation(location.getNameLocation());
        }
        if (location.getAddressLocation() != null) {
            existingLocation.setAddressLocation(location.getAddressLocation());
        }
        if (location.getAptSuite() != null) {
            existingLocation.setAptSuite(location.getAptSuite());
        }
        if (location.getCity() != null) {
            existingLocation.setCity(location.getCity());
        }
        if (location.getState() != null) {
            existingLocation.setState(location.getState());
        }
        if (location.getZip() != 0) {
            existingLocation.setZip(location.getZip());
        }
        if (location.getCountry() != null) {
            existingLocation.setCountry(location.getCountry());
        }
        if (location.getUsesNaturalGas() != existingLocation.getUsesNaturalGas()) {
            existingLocation.setUsesNaturalGas(location.getUsesNaturalGas());
        }
        if (location.getGrossArea() != 0) {
            existingLocation.setGrossArea(location.getGrossArea());
        }
        if (location.getSq() != null) {
            existingLocation.setSq(location.getSq());
        }
        if (location.getPrimaryUse() != null) {
            existingLocation.setPrimaryUse(location.getPrimaryUse());
        }
        if (location.getLatitude() != 0) {
            existingLocation.setLatitude(location.getLatitude());
        }
        if (location.getLongitude() != 0) {
            existingLocation.setLongitude(location.getLongitude());
        }
        if (location.getNaturalGasConsumption() != 0) {
            existingLocation.setNaturalGasConsumption(location.getNaturalGasConsumption());
        }
        if (location.getCo2Emissions() != 0) {
            existingLocation.setCo2Emissions(location.getCo2Emissions());
        }

        return locationRepository.save(existingLocation);
    }

    public Emission saveEmission(Emission emission, Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with ID: " + locationId));
        emission.setLocation(location);
        return emissionRepository.save(emission);
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
