package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record ExpenseRecord(
    long id,
    String type,
    double amount,
    String description,
    LocalDateTime timestamp
) {
    
    public ExpenseRecord {
        if (amount < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
    }
    
    /**
     * Convierte el registro a una línea CSV.
     * @return Línea en formato CSV
     */
    public String toCsvLine() {
        return String.format("%d,%s,%.2f,%s,%s",
            id, type, amount, description, timestamp);
    }
    
    /**
     * @return Cabecera para el archivo CSV
     */
    public static String getCsvHeader() {
        return "id,type,amount,description,timestamp";
    }
}