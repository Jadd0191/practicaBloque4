package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.config.ParkConfig;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrivalZone implements ParkZone {
    
    private final String name;
    private final int maxCapacity;
    private final double ticketPrice;
    private final Queue<Tourist> waitingQueue;  // Fila de espera
    private final List<Tourist> currentVisitors;  // Turistas dentro de la zona
    
    public ArrivalZone() {
        ParkConfig config = ParkConfig.getInstance();
        this.name = "Zona de Arribo";
        this.maxCapacity = config.getInt("arrival.maxCapacity", 30);
        this.ticketPrice = config.getDouble("arrival.ticketPrice", 25.0);
        this.waitingQueue = new LinkedList<>();
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
            tourist.setStatus(TouristStatus.IN_PARK);
        }
    }
    
    @Override
    public void exit(Tourist tourist) {
        if (tourist == null) return;
        currentVisitors.remove(tourist);
        tourist.setStatus(TouristStatus.EXITED);
    }
    
    /**
     * Agrega un turista a la fila de espera.
     * @param tourist Turista que llega al parque
     */
    public void addToQueue(Tourist tourist) {
        if (tourist != null && tourist.getStatus() == TouristStatus.WAITING) {
            waitingQueue.offer(tourist);
        }
    }
    
    /**
     * Procesa un lote de turistas: los saca de la fila, vende boletos y los hace entrar.
     * @param batchSize Cantidad de turistas a procesar
     * @param csvWriter Para registrar ingresos
     * @return Lista de turistas que entraron al parque
     */
    public List<Tourist> processBatch(int batchSize, CsvWriter csvWriter) {
        List<Tourist> arrived = new ArrayList<>();
        int processed = 0;
        
        while (processed < batchSize && !waitingQueue.isEmpty()) {
            Tourist tourist = waitingQueue.poll();
            if (tourist == null) continue;
            
            // Vender boleto
            tourist.spend(ticketPrice);
            tourist.recordVisit(name);
            
            // Registrar ingreso
            if (csvWriter != null) {
                RevenueRecord revenue = new RevenueRecord(
                    0,  // ID auto-generado por CSV
                    "TICKET",
                    ticketPrice,
                    tourist.getId(),
                    name,
                    LocalDateTime.now()
                );
                csvWriter.appendRevenue(revenue);
            }
            
            // Hacer entrar al turista
            enter(tourist);
            arrived.add(tourist);
            processed++;
        }
        
        return arrived;
    }
    
    /**
     * @return Cantidad de turistas en la fila de espera
     */
    public int getQueueSize() {
        return waitingQueue.size();
    }
    
    /**
     * @return Precio del boleto
     */
    public double getTicketPrice() {
        return ticketPrice;
    }
}