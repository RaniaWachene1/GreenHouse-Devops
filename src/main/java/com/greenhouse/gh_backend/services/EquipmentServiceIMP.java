package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Equipment;
import com.greenhouse.gh_backend.entities.User;
import com.greenhouse.gh_backend.repositories.EquipmentRepository;
import com.greenhouse.gh_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceIMP implements IEquipment {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Equipment> getEquipmentsByUserId(Long userId) {
        return equipmentRepository.findByUserIdUser(userId);
    }

    @Override
    public Equipment saveEquipment(Equipment equipment, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        equipment.setUser(user);
        return equipmentRepository.save(equipment);
    }

    @Override
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public Equipment updateEquipment(Equipment equipment, Long id) {
        Equipment existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        if (equipment.getEquipmentType() != null) {
            existingEquipment.setEquipmentType(equipment.getEquipmentType());
        }
        if (equipment.getEquipmentName() != null) {
            existingEquipment.setEquipmentName(equipment.getEquipmentName());
        }
        if (equipment.getEquipmentRef() != null) {
            existingEquipment.setEquipmentRef(equipment.getEquipmentRef());
        }
        if (equipment.getQuantity() != 0) {
            existingEquipment.setQuantity(equipment.getQuantity());
        }
        if (equipment.getPowerRating() != 0.0) {
            existingEquipment.setPowerRating(equipment.getPowerRating());
        }
        if (equipment.getAnnualUsageHours() != 0.0) {
            existingEquipment.setAnnualUsageHours(equipment.getAnnualUsageHours());
        }
        if (equipment.getCo2Emissions() != 0.0) {
            existingEquipment.setCo2Emissions(equipment.getCo2Emissions());
        }

        return equipmentRepository.save(existingEquipment);
    }

}
