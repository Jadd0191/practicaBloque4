package com.axity.dinosaurpark.zone;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PowerPlantMoreTest {
    
    @Test
    void testMultipleTicksUntilFailure() {
        PowerPlant plant = new PowerPlant();
        Random rng = new Random(42);
        
        // Consumir toda la energía
        for (int i = 0; i < 100; i++) {
            plant.tick(rng);
        }
        
        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getCurrentEnergy());
    }
    
    @Test
    void testRepairAfterFailure() {
        PowerPlant plant = new PowerPlant();
        Random rng = new Random(42);
        
        plant.triggerFailure();
        assertFalse(plant.isOperational());
        
        plant.repair();
        assertTrue(plant.isOperational());
        assertEquals(100.0, plant.getCurrentEnergy());
    }
    
    @Test
    void testEnergyPercentage() {
        PowerPlant plant = new PowerPlant();
        assertEquals(100.0, plant.getEnergyPercentage());
        
        Random rng = new Random(42);
        plant.tick(rng);
        assertTrue(plant.getEnergyPercentage() < 100.0);
    }
}