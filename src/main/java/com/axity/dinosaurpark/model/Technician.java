package com.axity.dinosaurpark.model;

import java.util.List;

import com.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker {
    
    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }
    
    @Override
    public String getRole() {
        return "TECHNICIAN";
    }
    
    // Versión básica (sin vehículo) - para compatibilidad
    public void repairIfNeeded(PowerPlant plant) {
        if (plant != null && !plant.isOperational()) {
            plant.repair();
        }
    }
    
    // Versión intermedio (con vehículo)
    public void repairIfNeeded(PowerPlant plant, List<Vehicle> vehicles) {
        if (plant == null || plant.isOperational()) return;
        
        Vehicle availableVehicle = vehicles.stream()
            .filter(Vehicle::isAvailable)
            .findFirst()
            .orElse(null);
        
        if (availableVehicle != null) {
            availableVehicle.use();
            plant.repair();
            availableVehicle.free();
        }
    }
}