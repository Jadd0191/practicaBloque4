package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.CsvWriter;

public class BathroomZoneTest {
    
    private BathroomZone bathroom;
    private Tourist tourist;
    private Random rng;
    private CsvWriter csvWriter;
    
    @BeforeEach
    void setUp() {
        bathroom = new BathroomZone();
        tourist = new Tourist(1, "Test");
        rng = new Random(42);
        csvWriter = null;
    }
    
    @Test
    void testTryEnter() {
        boolean entered = bathroom.tryEnter(tourist, rng, csvWriter);
        assertTrue(entered);
        assertEquals(1, bathroom.getCurrentOccupancy());
    }
    
    @Test
    void testTick() {
        bathroom.tryEnter(tourist, rng, csvWriter);
        assertEquals(1, bathroom.getCurrentOccupancy());
        
        // La duración es de 3 steps, necesitamos 3 ticks para liberar
        bathroom.tick();  // step 1
        assertEquals(1, bathroom.getCurrentOccupancy());
        
        bathroom.tick();  // step 2
        assertEquals(1, bathroom.getCurrentOccupancy());
        
        bathroom.tick();  // step 3
        assertEquals(0, bathroom.getCurrentOccupancy());  // Ahora sí debe ser 0
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