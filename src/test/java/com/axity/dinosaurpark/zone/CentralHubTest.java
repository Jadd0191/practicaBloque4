package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;

public class CentralHubTest {
    
    private CentralHub hub;
    private Tourist tourist;
    private Random rng;
    private CsvWriter csvWriter;
    
    @BeforeEach
    void setUp() {
        hub = new CentralHub();
        tourist = new Tourist(1, "Test");
        rng = new Random(42);
        csvWriter = null;
    }
    
    @Test
    void testVisit() {
        hub.visit(tourist, rng, csvWriter);
        assertEquals(1, tourist.getVisitedZones().size());
    }
    
    @Test
    void testGetName() {
        assertEquals("Recinto Central", hub.getName());
    }
}