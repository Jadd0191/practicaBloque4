package com.axity.dinosaurpark.zone;

import java.util.Random;

public enum ExperienceType {
    BASIC(1, 3),      // Puntuación mínima 1, máxima 3
    PREMIUM(2, 4),    // Puntuación mínima 2, máxima 4
    VIP(3, 5);        // Puntuación mínima 3, máxima 5
    
    private final int minScore;
    private final int maxScore;
    
    ExperienceType(int minScore, int maxScore) {
        this.minScore = minScore;
        this.maxScore = maxScore;
    }
    
    public int getMinScore() {
        return minScore;
    }
    
    public int getMaxScore() {
        return maxScore;
    }
    
    /**
     * Genera una puntuación aleatoria dentro del rango del tipo de experiencia.
     * @param rng Generador de números aleatorios
     * @return Puntuación entre minScore y maxScore (inclusive)
     */
    public int generateRandomScore(Random rng) {
        return rng.nextInt(maxScore - minScore + 1) + minScore;
    }
}
