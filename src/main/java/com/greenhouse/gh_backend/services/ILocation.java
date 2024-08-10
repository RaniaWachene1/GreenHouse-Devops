package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Location;

import java.util.List;

public interface ILocation {
    List<Location> getLocationsByUserId(Long idUser);

    List<Location> getLocationsByUserIdAndUsesNaturalGas(Long idUser);

    Double getTotalCo2EmissionsForUser(long userId);

    Location saveLocation(Location location , Long idUser);
    void deleteLocation(Long id);
    Location updateLocation(Location location, Long id);

}
