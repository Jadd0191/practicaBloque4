package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record EventRecord(
    long id,
    long step,
    String eventName,
    String description,
    String affectedEntities,
    LocalDateTime timestamp
) {
    
    /**
     * Convierte el registro a una línea CSV.
     * @return Línea en formato CSV
     */
    public String toCsvLine() {
        return String.format("%d,%d,%s,%s,%s,%s",
            id, step, eventName, description, affectedEntities, timestamp);
    }
    
    /**
     * @return Cabecera para el archivo CSV
     */
    public static String getCsvHeader() {
        return "id,step,eventName,description,affectedEntities,timestamp";
    }
}