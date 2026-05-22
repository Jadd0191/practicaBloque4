package com.axity.dinosaurpark.zone;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;

public class ArrivalZone implements ParkZone {
    
    private final String name = "Zona de Arribo";
    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingQueue = new LinkedList<>();
    private final List<Tourist> currentVisitors = new ArrayList<>();
    
    public ArrivalZone() {
        ParkConfig config = ParkConfig.getInstance();
        this.maxCapacity = config.getInt("arrival.maxCapacity", 30);
        this.ticketPrice = config.getDouble("arrival.ticketPrice", 25.0);
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
            tourist.setStatus(TouristStatus.IN_PARK);
        }
    }
    
    @Override
    public void exit(Tourist tourist) {
        if (tourist != null) {
            currentVisitors.remove(tourist);
            tourist.setStatus(TouristStatus.EXITED);
        }
    }
    
    public void addToQueue(Tourist tourist) {
        if (tourist != null && tourist.getStatus() == TouristStatus.WAITING) {
            waitingQueue.offer(tourist);
        }
    }
    
    public List<Tourist> processBatch(int batchSize) {
        List<Tourist> arrived = new ArrayList<>();
        int processed = 0;
        
        while (processed < batchSize && !waitingQueue.isEmpty()) {
            Tourist tourist = waitingQueue.poll();
            if (tourist != null) {
                tourist.spend(ticketPrice);
                tourist.recordVisit(name);
                enter(tourist);
                arrived.add(tourist);
                processed++;
            }
        }
        return arrived;
    }
    
    public List<Tourist> processBatch(int batchSize, double discount) {
        List<Tourist> arrived = new ArrayList<>();
        int processed = 0;
        double finalPrice = ticketPrice * (1 - discount);
        
        while (processed < batchSize && !waitingQueue.isEmpty()) {
            Tourist tourist = waitingQueue.poll();
            if (tourist != null) {
                tourist.spend(finalPrice);
                tourist.recordVisit(name);
                enter(tourist);
                arrived.add(tourist);
                processed++;
            }
        }
        return arrived;
    }
    
    public int getQueueSize() { return waitingQueue.size(); }
    public double getTicketPrice() { return ticketPrice; }
}