package com.axity.dinosaurpark.simulation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.config.ParkConfig;

public class SimulationEngineTest {
    
    private ParkConfig config;
    
    @BeforeEach
    void setUp() {
        // En lugar de resetear, simplemente obtenemos la instancia
        // Los tests de Singleton ya están cubiertos en ParkConfigTest
        config = ParkConfig.getInstance();
    }
    
    @Test
    void testEngineCreation() {
        SimulationEngine engine = new SimulationEngine(config);
        assertNotNull(engine);
    }
    
    @Test
    void testRunDoesNotThrowException() {
        SimulationEngine engine = new SimulationEngine(config);
        // Ejecutar la simulación completa
        assertDoesNotThrow(() -> engine.run());
    }
    
    @Test
    void testEngineWithDifferentConfig() {
        // Verificar que se puede crear con la configuración por defecto
        SimulationEngine engine = new SimulationEngine(config);
        assertNotNull(engine);
        
        // Verificar que los parámetros se leen correctamente
        assertTrue(config.getTotalSteps() > 0);
        assertTrue(config.getTotalTourists() > 0);
    }
}