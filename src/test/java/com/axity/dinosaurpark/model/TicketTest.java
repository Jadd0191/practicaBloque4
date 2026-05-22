package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TicketTest {
    
    @Test
    void testTicketCreation() {
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = new Ticket(1, 10, 25.0, "VIP", now);
        
        assertEquals(1, ticket.id());
        assertEquals(10, ticket.touristId());
        assertEquals(25.0, ticket.price());
        assertEquals("VIP", ticket.category());
        assertEquals(now, ticket.issuedAt());
    }
    
    @Test
    void testTicketWithDefaultCategory() {
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = new Ticket(1, 10, 25.0, now);
        
        assertEquals("STANDARD", ticket.category());
    }
    
    @Test
    void testTicketNegativePriceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ticket(1, 10, -25.0, LocalDateTime.now());
        });
    }
    
    @Test
    void testTicketToCsvLine() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);
        Ticket ticket = new Ticket(1, 10, 25.0, "NORMAL", now);
        
        String csvLine = ticket.toCsvLine();
        assertTrue(csvLine.contains("1,10,25.00,NORMAL"));
    }
}