package com.axity.dinosaurpark.event;

import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.persistence.EventRecord;
import java.util.Random;

public interface SimulationEvent {
    
    /**
     * @return Nombre del evento (ej: "ESCAPE_DINOSAURIO")
     */
    String getName();
    
    /**
     * @return Descripción del evento
     */
    String getDescription();
    
    /**
     * Ejecuta la lógica del evento.
     * @param state Estado actual del parque
     * @param rng Generador de números aleatorios
     */
    void execute(ParkState state, Random rng);
    
    /**
     * Convierte el evento a un registro para persistencia.
     * @param step Paso actual de la simulación
     * @return EventRecord para guardar en CSV
     */
    EventRecord toRecord(long step);
}
