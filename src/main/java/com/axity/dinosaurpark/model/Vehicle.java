package com.axity.dinosaurpark.model;

public class Vehicle {
    private final int id;
    private final String name;
    private VehicleStatus status;
    private int repairCountdown;
    private final int repairSteps;
    
    public Vehicle(int id, String name, int repairSteps) {
        this.id = id;
        this.name = name;
        this.status = VehicleStatus.AVAILABLE;
        this.repairSteps = repairSteps;
        this.repairCountdown = 0;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public VehicleStatus getStatus() { return status; }
    
    public void use() {
        if (status == VehicleStatus.AVAILABLE) {
            status = VehicleStatus.IN_USE;
        }
    }
    
    public void free() {
        if (status == VehicleStatus.IN_USE) {
            status = VehicleStatus.AVAILABLE;
        }
    }
    
    public void markBroken() {
        if (status == VehicleStatus.AVAILABLE) {
            status = VehicleStatus.BROKEN;
            repairCountdown = repairSteps;
        }
    }
    
    public void tick() {
        if (status == VehicleStatus.BROKEN) {
            repairCountdown--;
            if (repairCountdown <= 0) {
                status = VehicleStatus.AVAILABLE;
            }
        }
    }
    
    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }
}
