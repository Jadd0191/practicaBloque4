package com.axity.dinosaurpark.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class DinosaurEscapeEvent implements SimulationEvent {
    
    private final String name = "ESCAPE_DINOSAURIO";
    private final String description = "Un dinosaurio se ha escapado de su recinto";
    private String affectedEntities = "";
    private double probability;
    
    public DinosaurEscapeEvent(double probability) {
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
        List<Dinosaur> enclosured = state.getDinosaurs().stream()
            .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
            .collect(Collectors.toList());
        
        if (enclosured.isEmpty()) return;
        
        Dinosaur escaped = enclosured.get(rng.nextInt(enclosured.size()));
        escaped.escape();
        affectedEntities = "Dinosaurio: " + escaped.getName();
        
        if (rng.nextDouble() < escaped.getDangerLevel()) {
            List<Tourist> active = state.getTourists().stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                .collect(Collectors.toList());
            if (!active.isEmpty()) {
                Tourist attacked = active.get(rng.nextInt(active.size()));
                attacked.setStatus(TouristStatus.ATTACKED);
                affectedEntities += " | Atacó a: " + attacked.getName();
            }
        }
        
        if (state.getDb() != null) {
            state.getDb().appendEvent(toRecord(state.getCurrentStep()));
        }
    }
    
    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(0, step, name, description, affectedEntities, LocalDateTime.now());
    }
}