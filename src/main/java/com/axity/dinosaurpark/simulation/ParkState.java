package com.axity.dinosaurpark.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.ParkZone;
import com.axity.dinosaurpark.zone.PowerPlant;

public class ParkState {
    
    private long currentStep = 0;
    
    private final List<Tourist> tourists;
    private final List<Dinosaur> dinosaurs;
    private final List<Worker> workers;
    private final List<Vehicle> vehicles;
    
    private final ArrivalZone arrivalZone;
    private final CentralHub centralHub;
    private final BathroomZone bathroomZone;
    private final PowerPlant powerPlant;
    private final ObservationEnclosure basicEnclosure;
    private final ObservationEnclosure premiumEnclosure;
    private final ObservationEnclosure vipEnclosure;
    private final List<ParkZone> zones;
    
    private final DatabaseService db;
    private final Random rng;
    
    private double totalRevenue = 0.0;
    private double totalExpenses = 0.0;
    
    private final List<String> activeEventNames;
    private boolean dealsHourActive;
    private double currentDiscount;
    
    public ParkState(List<Tourist> tourists, List<Dinosaur> dinosaurs, List<Worker> workers,
                     List<Vehicle> vehicles,
                     ArrivalZone arrivalZone, CentralHub centralHub, BathroomZone bathroomZone,
                     PowerPlant powerPlant, ObservationEnclosure basicEnclosure,
                     ObservationEnclosure premiumEnclosure, ObservationEnclosure vipEnclosure,
                     DatabaseService db, long seed) {
        
        this.tourists = tourists;
        this.dinosaurs = dinosaurs;
        this.workers = workers;
        this.vehicles = vehicles;
        this.arrivalZone = arrivalZone;
        this.centralHub = centralHub;
        this.bathroomZone = bathroomZone;
        this.powerPlant = powerPlant;
        this.basicEnclosure = basicEnclosure;
        this.premiumEnclosure = premiumEnclosure;
        this.vipEnclosure = vipEnclosure;
        this.db = db;
        this.rng = new Random(); // Intermedio: sin semilla fija (no determinismo)
        
        this.activeEventNames = new ArrayList<>();
        this.dealsHourActive = false;
        this.currentDiscount = 0.0;
        
        this.zones = Arrays.asList(arrivalZone, centralHub, bathroomZone, 
                                    basicEnclosure, premiumEnclosure, vipEnclosure);
    }
    
    public long getCurrentStep() { return currentStep; }
    public List<Tourist> getTourists() { return tourists; }
    public List<Dinosaur> getDinosaurs() { return dinosaurs; }
    public List<Worker> getWorkers() { return workers; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public List<ParkZone> getZones() { return zones; }
    public ArrivalZone getArrivalZone() { return arrivalZone; }
    public CentralHub getCentralHub() { return centralHub; }
    public BathroomZone getBathroomZone() { return bathroomZone; }
    public PowerPlant getPowerPlant() { return powerPlant; }
    public ObservationEnclosure getBasicEnclosure() { return basicEnclosure; }
    public ObservationEnclosure getPremiumEnclosure() { return premiumEnclosure; }
    public ObservationEnclosure getVipEnclosure() { return vipEnclosure; }
    public DatabaseService getDb() { return db; }
    public Random getRng() { return rng; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalExpenses() { return totalExpenses; }
    
    public List<String> getActiveEventNames() { return activeEventNames; }
    public boolean isDealsHourActive() { return dealsHourActive; }
    public double getCurrentDiscount() { return currentDiscount; }
    
    public void setDealsHourActive(boolean active) { this.dealsHourActive = active; }
    public void setCurrentDiscount(double discount) { this.currentDiscount = discount; }
    
    public void addActiveEvent(String eventName) {
        if (eventName != null && !eventName.isEmpty()) {
            this.activeEventNames.add(eventName);
        }
    }
    
    public void clearActiveEvents() {
        this.activeEventNames.clear();
        this.dealsHourActive = false;
        this.currentDiscount = 0.0;
    }

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
    
    public int countVehiclesNotAvailable() {
        return (int) vehicles.stream()
            .filter(v -> v.getStatus() != VehicleStatus.AVAILABLE)
            .count();
    }
}