package com.axity.dinosaurpark.event;

import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class BlackoutEvent implements SimulationEvent {
    
    private final String name = "APAGON_MASIVO";
    private final String description = "Un apagón masivo ha afectado la planta de energía";
    private final String affectedEntities = "Planta de Energía";
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return description; }
    
    @Override
    public void execute(ParkState state, Random rng) {
        if (state.getPowerPlant() != null) {
            state.getPowerPlant().triggerFailure(state.getCsvWriter());  // ← pasar csvWriter
        }
        if (state.getCsvWriter() != null) {
            state.getCsvWriter().appendEvent(toRecord(state.getCurrentStep()));
        }
    }
    
    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(
            0, step, name, description, affectedEntities, LocalDateTime.now()
        );
    }
}
