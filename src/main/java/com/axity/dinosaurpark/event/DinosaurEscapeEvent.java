package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.model.*;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.persistence.EventRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DinosaurEscapeEvent implements SimulationEvent {
    
    private final String name = "ESCAPE_DINOSAURIO";
    private final String description = "Un dinosaurio se ha escapado de su recinto";
    private String affectedEntities = "";
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return description; }
    
    @Override
    public void execute(ParkState state, Random rng) {
        // Obtener dinosaurios que están en el recinto
        List<Dinosaur> enclosuredDinosaurs = state.getDinosaurs().stream()
            .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
            .collect(Collectors.toList());
        
        if (enclosuredDinosaurs.isEmpty()) return;
        
        // Elegir un dinosaurio al azar
        Dinosaur escaped = enclosuredDinosaurs.get(rng.nextInt(enclosuredDinosaurs.size()));
        escaped.escape();
        affectedEntities = "Dinosaurio: " + escaped.getName() + " (" + escaped.getSpecies() + ")";
        
        // Verificar ataque a turista
        if (rng.nextDouble() < escaped.getDangerLevel()) {
            List<Tourist> activeTourists = state.getTourists().stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                .collect(Collectors.toList());
            
            if (!activeTourists.isEmpty()) {
                Tourist attacked = activeTourists.get(rng.nextInt(activeTourists.size()));
                attacked.setStatus(TouristStatus.ATTACKED);
                affectedEntities += " | Turista atacado: " + attacked.getName();
            }
        }
        
        // Registrar evento en CSV
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