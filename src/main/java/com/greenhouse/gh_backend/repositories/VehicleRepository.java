package com.greenhouse.gh_backend.repositories;

import com.greenhouse.gh_backend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByUserIdUser(Long userId);
}

