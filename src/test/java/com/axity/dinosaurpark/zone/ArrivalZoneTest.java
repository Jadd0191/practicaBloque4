package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.CsvWriter;

public class ArrivalZoneTest {
    
    private ArrivalZone arrivalZone;
    private Tourist tourist;
    private CsvWriter csvWriter;
    
    @BeforeEach
    void setUp() {
        arrivalZone = new ArrivalZone();
        tourist = new Tourist(1, "Test");
        // Usar null para csvWriter en tests (no necesitamos guardar archivos)
        csvWriter = null;
    }
    
    @Test
    void testAddToQueue() {
        arrivalZone.addToQueue(tourist);
        assertEquals(1, arrivalZone.getQueueSize());
    }
    
    @Test
    void testProcessBatch() {
        arrivalZone.addToQueue(tourist);
        var arrived = arrivalZone.processBatch(1, csvWriter);
        assertEquals(1, arrived.size());
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
    }
    
    @Test
    void testProcessBatchWithNoTourists() {
        var arrived = arrivalZone.processBatch(5, csvWriter);
        assertEquals(0, arrived.size());
    }
    
    @Test
    void testGetTicketPrice() {
        assertTrue(arrivalZone.getTicketPrice() > 0);
    }
    
    @Test
    void testHasCapacity() {
        assertTrue(arrivalZone.hasCapacity());
    }
    
    @Test
    void testGetName() {
        assertEquals("Zona de Arribo", arrivalZone.getName());
    }
}