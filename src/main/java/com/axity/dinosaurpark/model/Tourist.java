package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.List;

public class Tourist {
    private final int id;
    private final String name;
    private TouristStatus status;
    private double moneySpent;
    private final List<String> visitedZones;
    
    /**
     * Creamos un nuevo turista.
     * @param id Identificador único
     * @param name Nombre del turista
     */
    public Tourist(int id, String name) {
        this.id = id;
        this.name = name;
        this.status = TouristStatus.WAITING;
        this.moneySpent = 0.0;
        this.visitedZones = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public TouristStatus getStatus() {
        return status;
    }
    
    public double getMoneySpent() {
        return moneySpent;
    }
    
    public List<String> getVisitedZones() {
        return new ArrayList<>(visitedZones);
    }
    
    public void setStatus(TouristStatus status) {
        this.status = status;
    }
    
    /**
     * Registra un gasto realizado por el turista.
     * @param amount Cantidad gastada (debe ser positiva)
     */
    public void spend(double amount) {
        if (amount > 0) {
            this.moneySpent += amount;
        }
    }
    
    /**
     * Registra que el turista visitó una zona.
     * @param zoneName Nombre de la zona visitada
     */
    public void recordVisit(String zoneName) {
        if (zoneName != null && !zoneName.isEmpty()) {
            this.visitedZones.add(zoneName);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Turista{id=%d, name='%s', status=%s, gastado=%.2f}",
                id, name, status, moneySpent);
    }
}
