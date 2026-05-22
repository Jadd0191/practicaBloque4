package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;

public class ObservationEnclosureTest {
    
    private ObservationEnclosure enclosure;
    private Tourist tourist;
    private Random rng;
    private CsvWriter csvWriter;
    
    @BeforeEach
    void setUp() {
        enclosure = new ObservationEnclosure("Test Enclosure", ExperienceType.BASIC);
        tourist = new Tourist(1, "Test");
        rng = new Random(42);
        csvWriter = null;
    }
    
    @Test
    void testVisit() {
        enclosure.visit(tourist, rng, csvWriter);
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
}