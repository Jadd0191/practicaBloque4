package com.axity.dinosaurpark.monitoring;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.CarnivoreDinosaur;
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

public class ParkMonitorTest {
    
    private ParkState state;
    
    @BeforeEach
    void setUp() {
        List<Tourist> tourists = new ArrayList<>();
        tourists.add(new Tourist(1, "Juan"));
        
        List<Dinosaur> dinosaurs = new ArrayList<>();
        dinosaurs.add(new CarnivoreDinosaur(1, "Rex", "T-Rex"));
        
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
        
        state.incrementStep();
        state.addRevenue(100.0);
        state.addExpense(50.0);
    }
    
    @Test
    void testDisplaySnapshotDoesNotThrowException() {
        assertDoesNotThrow(() -> ParkMonitor.displaySnapshot(state));
    }
}