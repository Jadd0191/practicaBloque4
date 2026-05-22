package com.axity.dinosaurpark.simulation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.ParkZone;
import com.axity.dinosaurpark.zone.PowerPlant;

/**
 * Contiene todo el estado global del parque.
 * Se pasa a los eventos y al monitor.
 */
public class ParkState {
    
    private long currentStep = 0;
    
    private final List<Tourist> tourists;
    private final List<Dinosaur> dinosaurs;
    private final List<Worker> workers;
    private final List<ParkZone> zones;
    
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlant powerPlant;
    private final ObservationEnclosure basicEnclosure;
    private final ObservationEnclosure premiumEnclosure;
    private final ObservationEnclosure vipEnclosure;
    
    private final CsvWriter csvWriter;
    private final Random rng;
    
    private double totalRevenue = 0.0;
    private double totalExpenses = 0.0;
    
    public ParkState(List<Tourist> tourists, List<Dinosaur> dinosaurs, List<Worker> workers,
                     ArrivalZone arrivalZone, CentralHub centralHub, BathroomZone bathroomZone,
                     PowerPlant powerPlant, ObservationEnclosure basicEnclosure,
                     ObservationEnclosure premiumEnclosure, ObservationEnclosure vipEnclosure,
                     CsvWriter csvWriter, long seed) {
        this.tourists = tourists;
        this.dinosaurs = dinosaurs;
        this.workers = workers;
        this.arrivalZone = arrivalZone;
        this.centralHub = centralHub;
        this.bathroomZone = bathroomZone;
        this.powerPlant = powerPlant;
        this.basicEnclosure = basicEnclosure;
        this.premiumEnclosure = premiumEnclosure;
        this.vipEnclosure = vipEnclosure;
        this.csvWriter = csvWriter;
        this.rng = new Random(seed);
        
        this.zones = Arrays.asList(arrivalZone, centralHub, bathroomZone, basicEnclosure, premiumEnclosure, vipEnclosure);
    }
    
    // Getters
    public long getCurrentStep() { return currentStep; }
    public List<Tourist> getTourists() { return tourists; }
    public List<Dinosaur> getDinosaurs() { return dinosaurs; }
    public List<Worker> getWorkers() { return workers; }
    public List<ParkZone> getZones() { return zones; }
    public ArrivalZone getArrivalZone() { return arrivalZone; }
    public CentralHub getCentralHub() { return centralHub; }
    public BathroomZone getBathroomZone() { return bathroomZone; }
    public PowerPlant getPowerPlant() { return powerPlant; }
    public ObservationEnclosure getBasicEnclosure() { return basicEnclosure; }
    public ObservationEnclosure getPremiumEnclosure() { return premiumEnclosure; }
    public ObservationEnclosure getVipEnclosure() { return vipEnclosure; }
    public CsvWriter getCsvWriter() { return csvWriter; }
    public Random getRng() { return rng; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalExpenses() { return totalExpenses; }
    
    public void incrementStep() { currentStep++; }
    public void addRevenue(double amount) { totalRevenue += amount; }
    public void addExpense(double amount) { totalExpenses += amount; }
    
    public int countActiveTourists() {
        return (int) tourists.stream()
            .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
            .count();
    }
    
    public int countDinosaursInEnclosure() {
        return (int) dinosaurs.stream()
            .filter(d -> d.getStatus() == DinosaurStatus.IN_ENCLOSURE)
            .count();
    }
}