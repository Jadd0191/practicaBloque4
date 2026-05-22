package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;

public class ObservationEnclosureTest {
    
    private ObservationEnclosure enclosure;
    private Tourist tourist;
    private Random rng;
    
    @BeforeEach
    void setUp() {
        enclosure = new ObservationEnclosure("Test Enclosure", ExperienceType.BASIC);
        tourist = new Tourist(1, "Test");
        rng = new Random(42);
    }
    
    @Test
    void testVisit() {
        enclosure.visit(tourist, rng);
        assertTrue(tourist.getMoneySpent() > 0);
        assertEquals(1, tourist.getVisitedZones().size());
    }
    
    @Test
    void testGetName() {
        assertEquals("Test Enclosure", enclosure.getName());
    }
    
    @Test
    void testGetEntryFee() {
        assertTrue(enclosure.getEntryFee() > 0);
    }
    
    @Test
    void testHasCapacity() {
        assertTrue(enclosure.hasCapacity());
    }
}