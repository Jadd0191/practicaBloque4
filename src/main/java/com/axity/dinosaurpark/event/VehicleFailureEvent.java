package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.persistence.EventRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class VehicleFailureEvent implements SimulationEvent {
    
    private final String name = "FALLA_VEHICULO";
    private final String description = "Un vehículo de mantenimiento ha fallado";
    private final double probability;
    private String affectedEntities = "";
    
    public VehicleFailureEvent(double probability) {
        this.probability = probability;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return description; }
    
    public double getProbability() { return probability; }
    
    @Override
    public void execute(ParkState state, Random rng) {
        List<Vehicle> availableVehicles = state.getVehicles().stream()
            .filter(v -> v.getStatus() == VehicleStatus.AVAILABLE)
            .collect(Collectors.toList());
        
        if (availableVehicles.isEmpty()) {
            affectedEntities = "No hay vehículos disponibles para fallar";
            return;
        }
        
        Vehicle broken = availableVehicles.get(rng.nextInt(availableVehicles.size()));
        broken.markBroken();
        affectedEntities = "Vehículo: " + broken.getName();
        
        if (state.getDb() != null) {
            state.getDb().appendEvent(toRecord(state.getCurrentStep()));
        }
    }
    
    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(0, step, name, description, affectedEntities, LocalDateTime.now());
    }
}