package com.axity.dinosaurpark.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CsvWriterTest {
    
    private CsvWriter csvWriter;
    private String testDir = "test_output";
    
    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriter(testDir);
    }
    
    @AfterEach
    void tearDown() throws IOException {
        csvWriter.close();
        // Limpiar archivos de prueba
        Path dir = Paths.get(testDir);
        if (Files.exists(dir)) {
            Files.walk(dir)
                .sorted((a, b) -> -1)
                .forEach(path -> {
                    try { Files.deleteIfExists(path); } catch (IOException e) {}
                });
        }
    }
    
    @Test
    void testAppendRevenue() throws IOException {
        RevenueRecord record = new RevenueRecord(0, "TICKET", 25.0, 1, "Arribo", LocalDateTime.now());
        csvWriter.appendRevenue(record);
        
        Path revenuePath = Paths.get(testDir, "revenues.csv");
        assertTrue(Files.exists(revenuePath));
        
        String content = Files.readString(revenuePath);
        assertTrue(content.contains("TICKET"));
        assertTrue(content.contains("25.0"));
    }
    
    @Test
    void testAppendExpense() throws IOException {
        ExpenseRecord record = new ExpenseRecord(0, "SALARY", 150.0, "Pago de salario", LocalDateTime.now());
        csvWriter.appendExpense(record);
        
        Path expensePath = Paths.get(testDir, "expenses.csv");
        assertTrue(Files.exists(expensePath));
        
        String content = Files.readString(expensePath);
        assertTrue(content.contains("SALARY"));
        assertTrue(content.contains("150.0"));
    }
    
    @Test
    void testAppendEvent() throws IOException {
        EventRecord record = new EventRecord(0, 1, "ESCAPE", "Dinosaurio escapado", "Rex", LocalDateTime.now());
        csvWriter.appendEvent(record);
        
        Path eventPath = Paths.get(testDir, "events.csv");
        assertTrue(Files.exists(eventPath));
        
        String content = Files.readString(eventPath);
        assertTrue(content.contains("ESCAPE"));
        assertTrue(content.contains("Dinosaurio escapado"));
    }
}