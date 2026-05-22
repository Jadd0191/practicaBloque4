package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.persistence.EventRecord;
import java.time.LocalDateTime;
import java.util.Random;

public class DealsHourEvent implements SimulationEvent {
    
    private final String name = "HORA_DE_OFERTAS";
    private final String description = "¡Hora de ofertas! 30% de descuento en boletos y souvenirs";
    private final double probability;
    
    public DealsHourEvent(double probability) {
        this.probability = probability;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return description; }
    
    public double getProbability() { return probability; }
    
    @Override
    public void execute(ParkState state, Random rng) {
        state.setDealsHourActive(true);
        state.setCurrentDiscount(0.30);
        
        if (state.getDb() != null) {
            state.getDb().appendEvent(toRecord(state.getCurrentStep()));
        }
    }
    
    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(0, step, name, description, "Descuento 30% activado", LocalDateTime.now());
    }
}