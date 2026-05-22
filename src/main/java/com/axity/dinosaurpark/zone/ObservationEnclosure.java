package com.axity.dinosaurpark.zone;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.SatisfactionSurvey;
import com.axity.dinosaurpark.model.Tourist;

public class ObservationEnclosure implements ParkZone {
    
    private final String name;
    private final ExperienceType type;
    private final int maxCapacity;
    private final double entryFee;
    private final List<Tourist> currentVisitors = new ArrayList<>();
    private final List<SatisfactionSurvey> surveys = new ArrayList<>();
    
    public ObservationEnclosure(String name, ExperienceType type) {
        ParkConfig config = ParkConfig.getInstance();
        this.name = name;
        this.type = type;
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
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public boolean hasCapacity() { return currentVisitors.size() < maxCapacity; }
    
    @Override
    public int getCurrentOccupancy() { return currentVisitors.size(); }
    
    @Override
    public int getMaxCapacity() { return maxCapacity; }
    
    @Override
    public void enter(Tourist tourist) {
        if (tourist != null && hasCapacity()) {
            currentVisitors.add(tourist);
            tourist.recordVisit(name);
        }
    }
    
    @Override
    public void exit(Tourist tourist) {
        if (tourist != null) {
            currentVisitors.remove(tourist);
        }
    }
    
    public void visit(Tourist tourist, Random rng) {
        if (tourist == null) return;
        if (!hasCapacity()) return;
        
        enter(tourist);
        tourist.spend(entryFee);
        
        int score = type.generateRandomScore(rng);
        surveys.add(new SatisfactionSurvey(0, tourist.getId(), name, score, LocalDateTime.now()));
        
        exit(tourist);
    }
    
    public List<SatisfactionSurvey> getSurveys() { return new ArrayList<>(surveys); }
    public double getAverageScore() {
        if (surveys.isEmpty()) return 0.0;
        double sum = surveys.stream().mapToDouble(SatisfactionSurvey::score).sum();
        return sum / surveys.size();
    }
    public ExperienceType getType() { return type; }
    public double getEntryFee() { return entryFee; }
}