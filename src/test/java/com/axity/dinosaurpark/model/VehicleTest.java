package com.axity.dinosaurpark.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VehicleTest {
    
    private Vehicle vehicle;
    
    @BeforeEach
    void setUp() {
        vehicle = new Vehicle(1, "Vehiculo1", 5);
    }
    
    @Test
    void testInitialStatus() {
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
        assertTrue(vehicle.isAvailable());
    }
    
    @Test
    void testUse() {
        vehicle.use();
        assertEquals(VehicleStatus.IN_USE, vehicle.getStatus());
        assertFalse(vehicle.isAvailable());
    }
    
    @Test
    void testFree() {
        vehicle.use();
        vehicle.free();
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }
    
    @Test
    void testMarkBroken() {
        vehicle.markBroken();
        assertEquals(VehicleStatus.BROKEN, vehicle.getStatus());
    }
    
    @Test
    void testTickRepair() {
        vehicle.markBroken();
        for (int i = 0; i < 5; i++) {
            vehicle.tick();
        }
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }
    
    @Test
    void testGetIdAndName() {
        assertEquals(1, vehicle.getId());
        assertEquals("Vehiculo1", vehicle.getName());
    }
}