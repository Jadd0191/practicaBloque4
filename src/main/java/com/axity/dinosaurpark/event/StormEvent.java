package com.axity.dinosaurpark.event;

import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class StormEvent implements SimulationEvent {
    
    private final String name = "TORMENTA_TORRENCIAL";
    private final String description = "Una tormenta torrencial obliga a evacuar zonas";
    private String affectedEntities = "";
    private double probability;
    
    public StormEvent(double probability) {
        this.probability = probability;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return description; }
    
    @Override
    public double getProbability() { return probability; }
    
    @Override
    public void execute(ParkState state, Random rng) {
        int evacuated = 0;
        for (Tourist t : state.getTourists()) {
            if (t.getStatus() == TouristStatus.IN_PARK) {
                t.recordVisit("Evacuación por tormenta");
                evacuated++;
            }
        }
        affectedEntities = evacuated + " turistas evacuados";
        
        if (state.getDb() != null) {
            ExpenseRecord expense = new ExpenseRecord(
                0, "EMERGENCY", 500.0,
                "Tormenta torrencial - gastos de evacuación",
                LocalDateTime.now()
            );
            state.getDb().appendExpense(expense);
            state.getDb().appendEvent(toRecord(state.getCurrentStep()));
        }
    }
    
    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(0, step, name, description, affectedEntities, LocalDateTime.now());
    }
}