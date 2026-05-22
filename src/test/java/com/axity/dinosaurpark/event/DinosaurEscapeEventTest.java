package com.axity.dinosaurpark.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

public class DinosaurEscapeEventTest {
    
    private DinosaurEscapeEvent event;
    private ParkState state;
    private List<Dinosaur> dinosaurs;
    private List<Tourist> tourists;
    private Random rng;
    
    @BeforeEach
    void setUp() {
        event = new DinosaurEscapeEvent();
        dinosaurs = new ArrayList<>();
        dinosaurs.add(new CarnivoreDinosaur(1, "Rex", "T-Rex"));
        dinosaurs.add(new HerbivoreDinosaur(2, "Trici", "Triceratops"));
        
        tourists = new ArrayList<>();
        tourists.add(new Tourist(1, "Juan"));
        tourists.add(new Tourist(2, "Maria"));
        
        rng = new Random(42);
        
        // Crear un ParkState mínimo para pruebas
        state = new ParkState(
            tourists, dinosaurs, new ArrayList<>(),
            new ArrivalZone(), new CentralHub(), new BathroomZone(),
            new PowerPlant(), 
            new ObservationEnclosure("Basic", ExperienceType.BASIC),
            new ObservationEnclosure("Premium", ExperienceType.PREMIUM),
            new ObservationEnclosure("VIP", ExperienceType.VIP),
            null, 42L
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("ESCAPE_DINOSAURIO", event.getName());
    }
    
    @Test
    void testGetDescription() {
        assertNotNull(event.getDescription());
    }
    
    @Test
    void testExecute() {
        event.execute(state, rng);
        
        // Algún dinosaurio debería estar escapado
        boolean algunEscapado = dinosaurs.stream()
            .anyMatch(d -> d.getStatus() == DinosaurStatus.ESCAPED);
        assertTrue(algunEscapado);
    }
    
    @Test
    void testToRecord() {
        var record = event.toRecord(10);
        assertEquals(10, record.step());
        assertEquals("ESCAPE_DINOSAURIO", record.eventName());
    }
}
