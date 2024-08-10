package com.greenhouse.gh_backend.restController;

import com.greenhouse.gh_backend.entities.Equipment;
import com.greenhouse.gh_backend.services.IEquipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = {"http://localhost:4200", "https://b94d-197-27-101-27.ngrok-free.app"}, maxAge = 3600, allowCredentials = "true")
public class EquipmentController {

    @Autowired
    private IEquipment equipmentService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Equipment>> getEquipmentsByUserId(@PathVariable Long userId) {
        List<Equipment> equipments = equipmentService.getEquipmentsByUserId(userId);
        return ResponseEntity.ok(equipments);
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<Equipment> saveEquipment(@RequestBody Equipment equipment, @PathVariable Long userId) {
        Equipment savedEquipment = equipmentService.saveEquipment(equipment, userId);
        return ResponseEntity.ok(savedEquipment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(@RequestBody Equipment equipment, @PathVariable Long id) {
        Equipment updatedEquipment = equipmentService.updateEquipment(equipment, id);
        return ResponseEntity.ok(updatedEquipment);
    }
}
