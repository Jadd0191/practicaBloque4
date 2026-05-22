package com.axity.dinosaurpark.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TouristTest {
    
    private Tourist tourist;
    
    @BeforeEach
    void setUp() {
        tourist = new Tourist(1, "Juan Perez");
    }
    
    @Test
    void testConstructor() {
        assertEquals(1, tourist.getId());
        assertEquals("Juan Perez", tourist.getName());
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
        assertEquals(0.0, tourist.getMoneySpent());
        assertTrue(tourist.getVisitedZones().isEmpty());
    }
    
    @Test
    void testSetStatus() {
        tourist.setStatus(TouristStatus.IN_PARK);
        assertEquals(TouristStatus.IN_PARK, tourist.getStatus());
        
        tourist.setStatus(TouristStatus.ATTACKED);
        assertEquals(TouristStatus.ATTACKED, tourist.getStatus());
    }
    
    @Test
    void testSpend() {
        tourist.spend(25.0);
        assertEquals(25.0, tourist.getMoneySpent());
        
        tourist.spend(10.5);
        assertEquals(35.5, tourist.getMoneySpent());
        
        tourist.spend(-5.0); // No debe sumar negativo
        assertEquals(35.5, tourist.getMoneySpent());
    }
    
    @Test
    void testRecordVisit() {
        tourist.recordVisit("Zona de Arribo");
        assertEquals(1, tourist.getVisitedZones().size());
        assertEquals("Zona de Arribo", tourist.getVisitedZones().get(0));
        
        tourist.recordVisit("Recinto Central");
        assertEquals(2, tourist.getVisitedZones().size());
    }
}