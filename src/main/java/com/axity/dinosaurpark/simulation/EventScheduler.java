package com.axity.dinosaurpark.simulation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;

public class EventScheduler {
    
    private final Map<Integer, SimulationEvent> scheduledEvents;
    private final Random rng;
    
    public EventScheduler(long seed, int totalSteps) {
        this.scheduledEvents = new HashMap<>();
        this.rng = new Random(seed);
        
        // Crear los 3 eventos
        List<SimulationEvent> events = Arrays.asList(
            new DinosaurEscapeEvent(),
            new BlackoutEvent(),
            new StormEvent()
        );
        
        // Programar cada evento en un step aleatorio
        for (SimulationEvent event : events) {
            // Step aleatorio entre 1 y totalSteps-1
            int step = rng.nextInt(totalSteps - 1) + 1;
            
            // Evitar que dos eventos ocurran en el mismo step
            while (scheduledEvents.containsKey(step)) {
                step = rng.nextInt(totalSteps - 1) + 1;
            }
            scheduledEvents.put(step, event);
        }
        
        System.out.println("EventScheduler inicializado con " + scheduledEvents.size() + " eventos");
    }
    
    /**
     * Verifica si hay un evento programado para el step actual.
     * @param step Paso actual de la simulación
     * @return Optional con el evento si existe, Optional.empty() si no
     */
    public Optional<SimulationEvent> checkForEvent(int step) {
        return Optional.ofNullable(scheduledEvents.get(step));
    }
    
    /**
     * @return Mapa de eventos programados (para depuración)
     */
    public Map<Integer, SimulationEvent> getScheduledEvents() {
        return new HashMap<>(scheduledEvents);
    }
}