package com.axity.dinosaurpark.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Dinosaur;
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

public class BlackoutEventTest {
    
    private BlackoutEvent event;
    private PowerPlant plant;
    private ParkState state;
    
    @BeforeEach
    void setUp() {
        event = new BlackoutEvent(0.03);
        plant = new PowerPlant();
        
        List<Tourist> tourists = new ArrayList<>();
        List<Dinosaur> dinosaurs = new ArrayList<>();
        List<Worker> workers = new ArrayList<>();
        List<Vehicle> vehicles = new ArrayList<>();
        
        state = new ParkState(
            tourists, dinosaurs, workers, vehicles,
            new ArrivalZone(), new CentralHub(), new BathroomZone(),
            plant,
            new ObservationEnclosure("Basic", ExperienceType.BASIC),
            new ObservationEnclosure("Premium", ExperienceType.PREMIUM),
            new ObservationEnclosure("VIP", ExperienceType.VIP),
            null, 0L
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("APAGON_MASIVO", event.getName());
    }
    
    @Test
    void testGetProbability() {
        assertEquals(0.03, event.getProbability());
    }
    
    @Test
    void testExecute() {
        assertTrue(plant.isOperational());
        
        event.execute(state, new Random());
        
        assertFalse(plant.isOperational());
        assertEquals(0.0, plant.getCurrentEnergy());
    }
    
    @Test
    void testToRecord() {
        var record = event.toRecord(5);
        assertEquals(5, record.step());
        assertEquals("APAGON_MASIVO", record.eventName());
    }
}