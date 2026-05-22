package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

public record SatisfactionSurvey(
    long id,                    // Identificador único de la encuesta
    int touristId,              // ID del turista que respondió
    String enclosureName,       // Nombre del encierro visitado
    int score,                  // Puntuación (1 a 5, donde 5 es excelente)
    LocalDateTime respondedAt   // Fecha y hora de la respuesta
) {
    
    public SatisfactionSurvey {
        if (touristId <= 0) {
            throw new IllegalArgumentException("El ID del turista debe ser positivo");
        }
        if (enclosureName == null || enclosureName.isBlank()) {
            throw new IllegalArgumentException("El nombre del encierro no puede estar vacío");
        }
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
    }
    
    /**
     * @return true si la puntuación es excelente (4 o 5)
     */
    public boolean isExcellent() {
        return score >= 4;
    }
    
    /**
     * @return true si la puntuación es mala (1 o 2)
     */
    public boolean isPoor() {
        return score <= 2;
    }
    
    /**
     * @return Representación en formato CSV (útil para persistencia)
     */
    public String toCsvLine() {
        return String.format("%d,%d,%s,%d,%s",
                id, touristId, enclosureName, score, respondedAt);
    }
}