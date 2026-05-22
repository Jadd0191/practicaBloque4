package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseServiceTest {
    
    private DatabaseService db;
    private String testDbPath = "./data/testdb";
    
    @BeforeEach
    void setUp() {
        db = new DatabaseService(testDbPath);
    }
    
    @AfterEach
    void tearDown() {
        if (db != null) {
            db.close();
        }
    }
    
    @Test
    void testAppendRevenue() {
        RevenueRecord record = new RevenueRecord(0, "TICKET", 25.0, 1, "Arribo", LocalDateTime.now());
        assertDoesNotThrow(() -> db.appendRevenue(record));
    }
    
    @Test
    void testAppendExpense() {
        ExpenseRecord record = new ExpenseRecord(0, "SALARY", 150.0, "Pago", LocalDateTime.now());
        assertDoesNotThrow(() -> db.appendExpense(record));
    }
    
    @Test
    void testAppendEvent() {
        EventRecord record = new EventRecord(0, 1, "TEST", "Evento de prueba", "", LocalDateTime.now());
        assertDoesNotThrow(() -> db.appendEvent(record));
    }
}