package com.axity.dinosaurpark.zone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;

public class ArrivalZoneTest {
    
    private ArrivalZone arrivalZone;
    private Tourist tourist;
    
    @BeforeEach
    void setUp() {
        arrivalZone = new ArrivalZone();
        tourist = new Tourist(1, "Test");
    }
    
    @Test
    void testAddToQueue() {
        arrivalZone.addToQueue(tourist);
        assertEquals(1, arrivalZone.getQueueSize());
    }
    
    @Test
    void testProcessBatch() {
        arrivalZone.addToQueue(tourist);
        var arrived = arrivalZone.processBatch(1);
        assertEquals(1, arrived.size());
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
    }
    
    @Test
    void testProcessBatchWithDiscount() {
        arrivalZone.addToQueue(tourist);
        var arrived = arrivalZone.processBatch(1, 0.30);
        assertEquals(1, arrived.size());
        assertEquals(17.5, tourist.getMoneySpent()); // 25 * 0.70 = 17.5
    }
    
    @Test
    void testProcessBatchWithNoTourists() {
        var arrived = arrivalZone.processBatch(5);
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