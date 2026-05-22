package com.axity.dinosaurpark.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DinosaurTest {
    
    private CarnivoreDinosaur carnivore;
    private HerbivoreDinosaur herbivore;
    
    @BeforeEach
    void setUp() {
        carnivore = new CarnivoreDinosaur(1, "Rex", "T-Rex");
        herbivore = new HerbivoreDinosaur(2, "Trici", "Triceratops");
    }
    
    @Test
    void testCarnivore() {
        assertEquals("CARNIVORE", carnivore.getDiet());
        assertEquals(0.9, carnivore.getDangerLevel());
        assertEquals(500.0, carnivore.getFeedingCostPerDay());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, carnivore.getStatus());
    }
    
    @Test
    void testHerbivore() {
        assertEquals("HERBIVORE", herbivore.getDiet());
        assertEquals(0.2, herbivore.getDangerLevel());
        assertEquals(200.0, herbivore.getFeedingCostPerDay());
        assertEquals(DinosaurStatus.IN_ENCLOSURE, herbivore.getStatus());
    }
    
    @Test
    void testEscape() {
        carnivore.escape();
        assertEquals(DinosaurStatus.ESCAPED, carnivore.getStatus());
    }
    
    @Test
    void testRecapture() {
        carnivore.escape();
        carnivore.recapture();
        assertEquals(DinosaurStatus.RECAPTURED, carnivore.getStatus());
    }
    
    @Test
    void testReturnToEnclosure() {
        carnivore.escape();
        carnivore.recapture();
        carnivore.returnToEnclosure();
        assertEquals(DinosaurStatus.IN_ENCLOSURE, carnivore.getStatus());
    }
}