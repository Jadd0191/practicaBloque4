package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.config.ParkConfig;

import java.time.LocalDateTime;
import java.util.*;

public class BathroomZone implements ParkZone {
    
    private final String name;
    private final int maxCapacity;
    private final int useDurationSteps;
    private final double spaPrice;
    private final double spaProbability;
    
    // Turistas actualmente en los baños y cuántos steps les quedan
    private final Map<Tourist, Integer> occupiedSlots;
    private final List<Tourist> currentVisitors;
    
    public BathroomZone() {
        ParkConfig config = ParkConfig.getInstance();
        this.name = "Baños";
        this.maxCapacity = config.getInt("bathroom.maxCapacity", 10);
        this.useDurationSteps = config.getInt("bathroom.useDurationSteps", 3);
        this.spaPrice = config.getDouble("bathroom.spaPrice", 20.0);
        this.spaProbability = config.getDouble("bathroom.spaPurchaseProbability", 0.2);
        this.occupiedSlots = new HashMap<>();
        this.currentVisitors = new ArrayList<>();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean hasCapacity() {
        return occupiedSlots.size() < maxCapacity;
    }
    
    @Override
    public int getCurrentOccupancy() {
        return occupiedSlots.size();
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
            occupiedSlots.put(tourist, useDurationSteps);
            tourist.recordVisit(name);
        }
    }
    
    @Override
    public void exit(Tourist tourist) {
        if (tourist == null) return;
        currentVisitors.remove(tourist);
        occupiedSlots.remove(tourist);
    }
    
    public void tick() {
        List<Tourist> toExit = new ArrayList<>();
        
        for (Map.Entry<Tourist, Integer> entry : occupiedSlots.entrySet()) {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                toExit.add(entry.getKey());
            } else {
                occupiedSlots.put(entry.getKey(), remaining);
            }
        }
        
        for (Tourist tourist : toExit) {
            exit(tourist);
        }
    }
    
    /**
     * Intenta que un turista entre al baño.
     * @param tourist Turista que quiere entrar
     * @param rng Generador de números aleatorios
     * @param csvWriter Para registrar ingresos
     * @return true si pudo entrar, false si no había capacidad
     */
    public boolean tryEnter(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null) return false;
        if (!hasCapacity()) return false;
        
        enter(tourist);
        
        if (rng.nextDouble() < spaProbability) {
            tourist.spend(spaPrice);
            
            if (csvWriter != null) {
                RevenueRecord revenue = new RevenueRecord(
                    0,
                    "SPA",
                    spaPrice,
                    tourist.getId(),
                    name,
                    LocalDateTime.now()
                );
                csvWriter.appendRevenue(revenue);
            }
        }
        
        return true;
    }
}