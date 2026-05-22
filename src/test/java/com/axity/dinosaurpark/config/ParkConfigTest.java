package com.axity.dinosaurpark.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParkConfigTest {
    
    private ParkConfig config;
    
    @BeforeEach
    void setUp() {
        ParkConfig.resetForTesting();
        config = ParkConfig.getInstance();
    }
    
    @Test
    void testSingletonReturnsSameInstance() {
        ParkConfig config2 = ParkConfig.getInstance();
        assertSame(config, config2);
    }
    
    @Test
    void testGetInt() {
        assertEquals(42, config.getInt("simulation.seed", 0));
        assertEquals(100, config.getInt("simulation.totalSteps", 0));
        assertEquals(99, config.getInt("clave.que.no.existe", 99));
    }
    
    @Test
    void testGetDouble() {
        assertEquals(25.0, config.getDouble("arrival.ticketPrice", 0.0));
        assertEquals(99.5, config.getDouble("clave.que.no.existe", 99.5));
    }
    
    @Test
    void testGetString() {
        assertEquals("output", config.getString("output.directory", ""));
        assertEquals("default", config.getString("clave.que.no.existe", "default"));
    }
    
    @Test
    void testGetSeed() {
        assertEquals(42L, config.getSeed());
    }
    
    @Test
    void testGetTotalSteps() {
        assertEquals(100, config.getTotalSteps());
    }
    
    @Test
    void testGetTotalTourists() {
        assertEquals(50, config.getTotalTourists());
    }
    
    @Test
    void testGetCarnivoreCount() {
        assertEquals(5, config.getCarnivoreCount());
    }
    
    @Test
    void testGetHerbivoreCount() {
        assertEquals(15, config.getHerbivoreCount());
    }
}
