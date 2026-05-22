package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.config.ParkConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObservationEnclosure implements ParkZone {
    
    private final String name;
    private final ExperienceType type;
    private final int maxCapacity;
    private final double entryFee;
    private final List<Tourist> currentVisitors;
    private final List<SatisfactionSurvey> surveys;
    
    public ObservationEnclosure(String name, ExperienceType type) {
        ParkConfig config = ParkConfig.getInstance();
        this.name = name;
        this.type = type;
        
        // Obtener capacidad y precio según el tipo
        switch (type) {
            case BASIC:
                this.maxCapacity = config.getInt("enclosure.basic.maxVisitors", 20);
                this.entryFee = config.getDouble("enclosure.basic.entryFee", 10.0);
                break;
            case PREMIUM:
                this.maxCapacity = config.getInt("enclosure.premium.maxVisitors", 12);
                this.entryFee = config.getDouble("enclosure.premium.entryFee", 30.0);
                break;
            case VIP:
                this.maxCapacity = config.getInt("enclosure.vip.maxVisitors", 5);
                this.entryFee = config.getDouble("enclosure.vip.entryFee", 75.0);
                break;
            default:
                this.maxCapacity = 20;
                this.entryFee = 10.0;
        }
        
        this.currentVisitors = new ArrayList<>();
        this.surveys = new ArrayList<>();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean hasCapacity() {
        return currentVisitors.size() < maxCapacity;
    }
    
    @Override
    public int getCurrentOccupancy() {
        return currentVisitors.size();
    }
    
    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    @Override
    public void enter(Tourist tourist) {
        if (tourist == null) return;
        if (hasCapacity()) {
            currentVisitors.add(tourist);
            tourist.recordVisit(name);
        }
    }
    
    @Override
    public void exit(Tourist tourist) {
        if (tourist == null) return;
        currentVisitors.remove(tourist);
    }
    
    /**
     * Un turista visita el encierro.
     * @param tourist Turista que visita
     * @param rng Generador de números aleatorios
     * @param csvWriter Para registrar ingresos y encuestas
     */
    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null) return;
        if (!hasCapacity()) return;
        
        enter(tourist);
        
        // Cobrar entrada
        tourist.spend(entryFee);
        
        if (csvWriter != null) {
            RevenueRecord revenue = new RevenueRecord(
                0,
                "ENCLOSURE_" + type.name(),
                entryFee,
                tourist.getId(),
                name,
                LocalDateTime.now()
            );
            csvWriter.appendRevenue(revenue);
        }
        
        // Realizar encuesta de satisfacción
        int score = type.generateRandomScore(rng);
        SatisfactionSurvey survey = new SatisfactionSurvey(
            0,
            tourist.getId(),
            name,
            score,
            LocalDateTime.now()
        );
        surveys.add(survey);
        
        if (csvWriter != null) {
            // Aquí se podría guardar la encuesta en CSV también
            // Por ahora solo la guardamos en memoria
        }
        
        exit(tourist);
    }
    
    /**
     * @return Lista de todas las encuestas realizadas en este encierro
     */
    public List<SatisfactionSurvey> getSurveys() {
        return new ArrayList<>(surveys);
    }
    
    /**
     * @return Puntuación promedio de las encuestas
     */
    public double getAverageScore() {
        if (surveys.isEmpty()) return 0.0;
        double sum = 0.0;
        for (SatisfactionSurvey survey : surveys) {
            sum += survey.score();
        }
        return sum / surveys.size();
    }
    
    public ExperienceType getType() {
        return type;
    }
    
    public double getEntryFee() {
        return entryFee;
    }
}
