package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

public record Ticket(
    long id,
    int touristId,
    double price,
    String category,
    LocalDateTime issuedAt
) {
    
    public Ticket {
        if (price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (touristId <= 0) {
            throw new IllegalArgumentException("El ID del turista debe ser positivo");
        }
    }
    
    public Ticket(long id, int touristId, double price, LocalDateTime issuedAt) {
        this(id, touristId, price, "STANDARD", issuedAt);
    }
    
    /**
     * @return Representación en formato CSV (útil para persistencia)
     */
    public String toCsvLine() {
        return String.format("%d,%d,%.2f,%s,%s",
                id, touristId, price, category, issuedAt);
    }
}