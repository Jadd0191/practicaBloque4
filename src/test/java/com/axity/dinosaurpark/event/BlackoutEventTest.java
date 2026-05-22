package com.axity.dinosaurpark.event;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        event = new BlackoutEvent();
        plant = new PowerPlant();
        
        state = new ParkState(
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrivalZone(), new CentralHub(), new BathroomZone(),
            plant,
            new ObservationEnclosure("Basic", ExperienceType.BASIC),
            new ObservationEnclosure("Premium", ExperienceType.PREMIUM),
            new ObservationEnclosure("VIP", ExperienceType.VIP),
            null, 42L
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("APAGON_MASIVO", event.getName());
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