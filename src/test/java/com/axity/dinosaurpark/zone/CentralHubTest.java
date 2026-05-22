package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;

public class CentralHubTest {
    
    private CentralHub hub;
    private Tourist tourist;
    private Random rng;
    
    @BeforeEach
    void setUp() {
        hub = new CentralHub();
        tourist = new Tourist(1, "Test");
        rng = new Random(42);
    }
    
    @Test
    void testVisit() {
        hub.visit(tourist, rng);
        assertEquals(1, tourist.getVisitedZones().size());
    }
    
    @Test
    void testGetName() {
        assertEquals("Recinto Central", hub.getName());
    }
    
    @Test
    void testHasCapacity() {
        assertTrue(hub.hasCapacity());
    }
}