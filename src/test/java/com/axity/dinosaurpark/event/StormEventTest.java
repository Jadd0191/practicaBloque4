package com.axity.dinosaurpark.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

public class StormEventTest {
    
    private StormEvent event;
    private List<Tourist> tourists;
    private ParkState state;
    
    @BeforeEach
    void setUp() {
        event = new StormEvent();
        tourists = new ArrayList<>();
        tourists.add(new Tourist(1, "Juan"));
        tourists.add(new Tourist(2, "Maria"));
        
        // Poner turistas en el parque
        for (Tourist t : tourists) {
            t.setStatus(TouristStatus.IN_PARK);
        }
        
        state = new ParkState(
            tourists, new ArrayList<>(), new ArrayList<>(),
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
        assertEquals("TORMENTA_TORRENCIAL", event.getName());
    }
    
    @Test
    void testExecute() {
        event.execute(state, new Random());
        
        // Verificar que los turistas registraron evacuación
        for (Tourist t : tourists) {
            assertTrue(t.getVisitedZones().stream()
                .anyMatch(z -> z.contains("Evacuación")));
        }
    }
    
    @Test
    void testToRecord() {
        var record = event.toRecord(3);
        assertEquals(3, record.step());
        assertEquals("TORMENTA_TORRENCIAL", record.eventName());
    }
}
