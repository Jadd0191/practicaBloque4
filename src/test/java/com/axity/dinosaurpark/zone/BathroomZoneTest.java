package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;

public class BathroomZoneTest {
    
    private BathroomZone bathroom;
    private Tourist tourist;
    private Random rng;
    
    @BeforeEach
    void setUp() {
        bathroom = new BathroomZone();
        tourist = new Tourist(1, "Test");
        rng = new Random(42);
    }
    
    @Test
    void testTryEnter() {
        boolean entered = bathroom.tryEnter(tourist, rng);
        assertTrue(entered);
        assertEquals(1, bathroom.getCurrentOccupancy());
    }
    
    @Test
    void testTick() {
        bathroom.tryEnter(tourist, rng);
        assertEquals(1, bathroom.getCurrentOccupancy());
        
        bathroom.tick(); // step 1
        bathroom.tick(); // step 2
        bathroom.tick(); // step 3
        assertEquals(0, bathroom.getCurrentOccupancy());
    }
    
    @Test
    void testGetName() {
        assertEquals("Baños", bathroom.getName());
    }
    
    @Test
    void testHasCapacity() {
        assertTrue(bathroom.hasCapacity());
    }
    
    @Test
    void testGetMaxCapacity() {
        assertTrue(bathroom.getMaxCapacity() > 0);
    }
}