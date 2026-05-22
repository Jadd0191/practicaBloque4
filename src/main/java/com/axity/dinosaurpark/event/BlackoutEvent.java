package com.axity.dinosaurpark.event;

import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class BlackoutEvent implements SimulationEvent {
    
    private final String name = "APAGON_MASIVO";
    private final String description = "Un apagón masivo ha afectado la planta de energía";
    private final String affectedEntities = "Planta de Energía";
    private double probability = 0.03;
    
    public BlackoutEvent() {
    }
    
    public BlackoutEvent(double probability) {
        this.probability = probability;
    }
    
    @Override
    public String getName() { 
        return name; 
    }
    
    @Override
    public String getDescription() { 
        return description; 
    }
    
    public double getProbability() { 
        return probability; 
    }
    
    public void setProbability(double probability) { 
        this.probability = probability; 
    }
    
    @Override
    public void execute(ParkState state, Random rng) {
        if (state.getPowerPlant() != null) {
            state.getPowerPlant().triggerFailure();
            
            // Registrar gasto de reparación por apagón
            if (state.getDb() != null) {
                ExpenseRecord expense = new ExpenseRecord(
                    0, 
                    "EMERGENCY", 
                    2000.0,
                    "Apagón masivo - reparación de emergencia",
                    LocalDateTime.now()
                );
                state.getDb().appendExpense(expense);
            }
        }
        
        // Registrar el evento en la base de datos
        if (state.getDb() != null) {
            state.getDb().appendEvent(toRecord(state.getCurrentStep()));
        }
    }
    
    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(
            0, 
            step, 
            name, 
            description, 
            affectedEntities, 
            LocalDateTime.now()
        );
    }
}