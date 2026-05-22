package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PowerPlantTest {
    
    private PowerPlant plant;
    private Random rng;
    
    @BeforeEach
    void setUp() {
        plant = new PowerPlant();
        rng = new Random(42);
    }
    
    @Test
    void testInitialState() {
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getCurrentEnergy());
        assertEquals(100.0, plant.getEnergyPercentage());
    }
    
    @Test
    void testTickConsumesEnergy() {
        plant.tick(rng);
        assertTrue(plant.getCurrentEnergy() < 100.0);
        assertTrue(plant.isOperational());
    }
    
    @Test
    void testTriggerFailure() {
        plant.triggerFailure();
        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getCurrentEnergy());
    }
    
    @Test
    void testRepair() {
        plant.triggerFailure();
        plant.repair();
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getCurrentEnergy());
    }
}