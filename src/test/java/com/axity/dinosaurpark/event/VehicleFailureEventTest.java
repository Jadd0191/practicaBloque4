package com.axity.dinosaurpark.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.VehicleStatus;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

public class VehicleFailureEventTest {
    
    private VehicleFailureEvent event;
    private List<Vehicle> vehicles;
    private ParkState state;
    
    @BeforeEach
    void setUp() {
        event = new VehicleFailureEvent(0.06);
        
        vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(1, "V1", 5));
        vehicles.add(new Vehicle(2, "V2", 5));
        
        state = new ParkState(
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), vehicles,
            new ArrivalZone(), new CentralHub(), new BathroomZone(),
            new PowerPlant(),
            new ObservationEnclosure("Basic", ExperienceType.BASIC),
            new ObservationEnclosure("Premium", ExperienceType.PREMIUM),
            new ObservationEnclosure("VIP", ExperienceType.VIP),
            null, 0L
        );
    }
    
    @Test
    void testGetName() {
        assertEquals("FALLA_VEHICULO", event.getName());
    }
    
    @Test
    void testGetProbability() {
        assertEquals(0.06, event.getProbability());
    }
    
    @Test
    void testExecuteBreaksVehicle() {
        int availableBefore = (int) vehicles.stream().filter(Vehicle::isAvailable).count();
        assertEquals(2, availableBefore);
        
        event.execute(state, new Random());
        
        int availableAfter = (int) vehicles.stream().filter(Vehicle::isAvailable).count();
        assertEquals(1, availableAfter);
        
        long brokenCount = vehicles.stream()
            .filter(v -> v.getStatus() == VehicleStatus.BROKEN)
            .count();
        assertEquals(1, brokenCount);
    }
    
    @Test
    void testExecuteWithNoAvailableVehicles() {
        // Marcar todos los vehículos como IN_USE
        for (Vehicle v : vehicles) {
            v.use();
        }
        
        int availableBefore = (int) vehicles.stream().filter(Vehicle::isAvailable).count();
        assertEquals(0, availableBefore);
        
        // No debe lanzar excepción
        assertDoesNotThrow(() -> event.execute(state, new Random()));
    }
}