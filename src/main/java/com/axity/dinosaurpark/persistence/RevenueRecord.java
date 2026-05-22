package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record RevenueRecord(
    long id,
    String type,
    double amount,
    int touristId,
    String zone,
    LocalDateTime timestamp
) {
    
    public RevenueRecord {
        if (amount < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
    }
    
    /**
     * Convierte el registro a una línea CSV.
     * @return Línea en formato CSV
     */
    public String toCsvLine() {
        return String.format("%d,%s,%.2f,%d,%s,%s",
            id, type, amount, touristId, zone, timestamp);
    }
    
    /**
     * @return Cabecera para el archivo CSV
     */
    public static String getCsvHeader() {
        return "id,type,amount,touristId,zone,timestamp";
    }
}
