package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkerTest {
    
    private Guard guard;
    private Technician technician;
    private List<Dinosaur> dinosaurs;
    
    @BeforeEach
    void setUp() {
        guard = new Guard(1, "Guardia1", 150.0);
        technician = new Technician(2, "Tecnico1", 150.0);
        dinosaurs = new ArrayList<>();
        dinosaurs.add(new CarnivoreDinosaur(1, "Rex", "T-Rex"));
        dinosaurs.add(new HerbivoreDinosaur(2, "Trici", "Triceratops"));
    }
    
    @Test
    void testGuardConstructor() {
        assertEquals(1, guard.getId());
        assertEquals("Guardia1", guard.getName());
        assertEquals(150.0, guard.getDailySalary());
        assertEquals("GUARD", guard.getRole());
    }
    
    @Test
    void testTechnicianConstructor() {
        assertEquals(2, technician.getId());
        assertEquals("Tecnico1", technician.getName());
        assertEquals(150.0, technician.getDailySalary());
        assertEquals("TECHNICIAN", technician.getRole());
    }
    
    @Test
    void testRecaptureEscapedDinosaurs() {
        // Escape un dinosaurio
        dinosaurs.get(0).escape();
        assertEquals(DinosaurStatus.ESCAPED, dinosaurs.get(0).getStatus());
        
        // Guardia recaptura
        guard.recaptureEscapedDinosaurs(dinosaurs);
        assertEquals(DinosaurStatus.RECAPTURED, dinosaurs.get(0).getStatus());
    }
}