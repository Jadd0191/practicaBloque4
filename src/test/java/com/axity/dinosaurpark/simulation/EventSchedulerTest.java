package com.axity.dinosaurpark.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class EventSchedulerTest {
    
    @Test
    void testSchedulerCreatesEvents() {
        EventScheduler scheduler = new EventScheduler(42L, 100);
        
        // Debe haber 3 eventos programados
        int eventCount = 0;
        for (int i = 1; i <= 100; i++) {
            if (scheduler.checkForEvent(i).isPresent()) {
                eventCount++;
            }
        }
        assertEquals(3, eventCount);
    }
    
    @Test
    void testSameSeedGivesSameEvents() {
        EventScheduler scheduler1 = new EventScheduler(42L, 100);
        EventScheduler scheduler2 = new EventScheduler(42L, 100);
        
        for (int i = 1; i <= 100; i++) {
            boolean hasEvent1 = scheduler1.checkForEvent(i).isPresent();
            boolean hasEvent2 = scheduler2.checkForEvent(i).isPresent();
            assertEquals(hasEvent1, hasEvent2);
        }
    }
    
    @Test
    void testDifferentSeedGivesDifferentEvents() {
        EventScheduler scheduler1 = new EventScheduler(42L, 100);
        EventScheduler scheduler2 = new EventScheduler(99L, 100);
        
        boolean allSame = true;
        for (int i = 1; i <= 100; i++) {
            boolean hasEvent1 = scheduler1.checkForEvent(i).isPresent();
            boolean hasEvent2 = scheduler2.checkForEvent(i).isPresent();
            if (hasEvent1 != hasEvent2) {
                allSame = false;
                break;
            }
        }
        assertFalse(allSame);
    }
}