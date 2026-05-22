package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;

public interface ParkZone {
    
    /**
     * @return Nombre de la zona (ej: "Zona de Arribo", "Recinto Central")
     */
    String getName();
    
    /**
     * @return true si hay espacio disponible para más turistas
     */
    boolean hasCapacity();
    
    /**
     * @return Número actual de turistas en la zona
     */
    int getCurrentOccupancy();
    
    /**
     * @return Capacidad máxima de la zona
     */
    int getMaxCapacity();
    
    /**
     * Un turista entra a la zona.
     * @param tourist Turista que entra
     */
    void enter(Tourist tourist);
    
    /**
     * Un turista sale de la zona.
     * @param tourist Turista que sale
     */
    void exit(Tourist tourist);
}