package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Equipment;

import java.util.List;

public interface IEquipment {
    List<Equipment> getEquipmentsByUserId(Long userId);
    Equipment saveEquipment(Equipment equipment, Long userId);
    void deleteEquipment(Long id);
    Equipment updateEquipment(Equipment equipment, Long id);

}
