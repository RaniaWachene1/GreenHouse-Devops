package com.greenhouse.gh_backend.repositories;

import com.greenhouse.gh_backend.entities.Equipment;
import com.greenhouse.gh_backend.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByUserIdUser(Long userId);

}