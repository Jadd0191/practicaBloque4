package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import java.time.LocalDateTime;
import java.util.Random;

public class StormEvent implements SimulationEvent {
    
    private final String name = "TORMENTA_TORRENCIAL";
    private final String description = "Una tormenta torrencial obliga a evacuar zonas";
    private String affectedEntities = "";
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return description; }
    
    @Override
    public void execute(ParkState state, Random rng) {
        int evacuatedCount = 0;
        
        for (Tourist tourist : state.getTourists()) {
            if (tourist.getStatus() == TouristStatus.IN_PARK) {
                tourist.recordVisit("Evacuación por tormenta");
                evacuatedCount++;
            }
        }
        
        affectedEntities = evacuatedCount + " turistas evacuados";
        
        // Registrar gasto de $500
        if (state.getCsvWriter() != null) {
            ExpenseRecord expense = new ExpenseRecord(
                0, "EMERGENCY", 500.0,
                "Tormenta torrencial - gastos de evacuación",
                LocalDateTime.now()
            );
            state.getCsvWriter().appendExpense(expense);
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