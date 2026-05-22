package com.axity.dinosaurpark.zone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;

public class CentralHub implements ParkZone {
    
    private final String name = "Recinto Central";
    private final int maxCapacity = 100;
    private final double souvenirPrice;
    private final double souvenirProbability;
    private final List<Tourist> currentVisitors = new ArrayList<>();
    
    public CentralHub() {
        ParkConfig config = ParkConfig.getInstance();
        this.souvenirPrice = config.getDouble("hub.souvenirPrice", 15.0);
        this.souvenirProbability = config.getDouble("hub.souvenirPurchaseProbability", 0.4);
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
        enter(tourist);
        if (rng.nextDouble() < souvenirProbability) {
            tourist.spend(souvenirPrice);
            tourist.recordVisit(name + " - Souvenir");
        }
        exit(tourist);
    }
}