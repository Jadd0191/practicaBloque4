package com.axity.dinosaurpark.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.Worker;
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
        event = new DinosaurEscapeEvent(0.05);
        dinosaurs = new ArrayList<>();
        dinosaurs.add(new CarnivoreDinosaur(1, "Rex", "T-Rex"));
        dinosaurs.add(new HerbivoreDinosaur(2, "Trici", "Triceratops"));
        
        tourists = new ArrayList<>();
        tourists.add(new Tourist(1, "Juan"));
        tourists.add(new Tourist(2, "Maria"));
        
        List<Worker> workers = new ArrayList<>();
        List<Vehicle> vehicles = new ArrayList<>();
        
        state = new ParkState(
            tourists, dinosaurs, workers, vehicles,
            new ArrivalZone(), new CentralHub(), new BathroomZone(),
            new PowerPlant(),
            new ObservationEnclosure("Basic", ExperienceType.BASIC),
            new ObservationEnclosure("Premium", ExperienceType.PREMIUM),
            new ObservationEnclosure("VIP", ExperienceType.VIP),
            null, 0L
        );
        
        rng = new Random();
    }
    
    @Test
    void testGetName() {
        assertEquals("ESCAPE_DINOSAURIO", event.getName());
    }
    
    @Test
    void testGetProbability() {
        assertEquals(0.05, event.getProbability());
    }
    
    @Test
    void testExecute() {
        event.execute(state, rng);
        
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