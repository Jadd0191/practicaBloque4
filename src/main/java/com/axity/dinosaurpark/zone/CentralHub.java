package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.config.ParkConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CentralHub implements ParkZone {
    
    private final String name;
    private final int maxCapacity;
    private final double souvenirPrice;
    private final double souvenirProbability;
    private final List<Tourist> currentVisitors;
    
    public CentralHub() {
        ParkConfig config = ParkConfig.getInstance();
        this.name = "Recinto Central";
        this.maxCapacity = 100;  // Capacidad grande, sin límite estricto
        this.souvenirPrice = config.getDouble("hub.souvenirPrice", 15.0);
        this.souvenirProbability = config.getDouble("hub.souvenirPurchaseProbability", 0.4);
        this.currentVisitors = new ArrayList<>();
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
     * Un turista visita el hub. Puede comprar un souvenir con cierta probabilidad.
     * @param tourist Turista que visita
     * @param rng Generador de números aleatorios
     * @param csvWriter Para registrar ingresos
     */
    public void visit(Tourist tourist, Random rng, CsvWriter csvWriter) {
        if (tourist == null) return;
        
        enter(tourist);
        
        // Intentar comprar souvenir
        if (rng.nextDouble() < souvenirProbability) {
            tourist.spend(souvenirPrice);
            tourist.recordVisit(name + " - Souvenir");
            
            if (csvWriter != null) {
                RevenueRecord revenue = new RevenueRecord(
                    0,
                    "SOUVENIR",
                    souvenirPrice,
                    tourist.getId(),
                    name,
                    LocalDateTime.now()
                );
                csvWriter.appendRevenue(revenue);
            }
        }
        
        exit(tourist);
    }
}