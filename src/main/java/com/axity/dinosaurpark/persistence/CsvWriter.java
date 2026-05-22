package com.axity.dinosaurpark.persistence;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

public class CsvWriter implements AutoCloseable {
    
    private final String outputDir;
    private final AtomicLong revenueId = new AtomicLong(1);
    private final AtomicLong expenseId = new AtomicLong(1);
    private final AtomicLong eventId = new AtomicLong(1);
    
    private PrintWriter revenueWriter;
    private PrintWriter expenseWriter;
    private PrintWriter eventWriter;
    
    public CsvWriter(String outputDir) {
        this.outputDir = outputDir;
        initFiles();
    }
    
    /**
     * Inicializa los archivos CSV (sobrescribe si existen).
     */
    private void initFiles() {
        try {
            // Crear directorio si no existe
            Path dir = Paths.get(outputDir);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            
            // Archivo de ingresos
            Path revenuePath = dir.resolve("revenues.csv");
            revenueWriter = new PrintWriter(Files.newBufferedWriter(revenuePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
            revenueWriter.println(RevenueRecord.getCsvHeader());
            
            // Archivo de gastos
            Path expensePath = dir.resolve("expenses.csv");
            expenseWriter = new PrintWriter(Files.newBufferedWriter(expensePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
            expenseWriter.println(ExpenseRecord.getCsvHeader());
            
            // Archivo de eventos
            Path eventPath = dir.resolve("events.csv");
            eventWriter = new PrintWriter(Files.newBufferedWriter(eventPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
            eventWriter.println(EventRecord.getCsvHeader());
            
            System.out.println("Archivos CSV inicializados en: " + outputDir);
            
        } catch (IOException e) {
            System.err.println("Error al inicializar archivos CSV: " + e.getMessage());
        }
    }
    
    /**
     * Agrega un registro de ingreso al CSV.
     * @param record Registro de ingreso
     */
    public void appendRevenue(RevenueRecord record) {
        if (revenueWriter != null) {
            RevenueRecord recordWithId = new RevenueRecord(
                revenueId.getAndIncrement(),
                record.type(),
                record.amount(),
                record.touristId(),
                record.zone(),
                record.timestamp()
            );
            revenueWriter.println(recordWithId.toCsvLine());
            revenueWriter.flush();
        }
    }
    
    /**
     * Agrega un registro de gasto al CSV.
     * @param record Registro de gasto
     */
    public void appendExpense(ExpenseRecord record) {
        if (expenseWriter != null) {
            ExpenseRecord recordWithId = new ExpenseRecord(
                expenseId.getAndIncrement(),
                record.type(),
                record.amount(),
                record.description(),
                record.timestamp()
            );
            expenseWriter.println(recordWithId.toCsvLine());
            expenseWriter.flush();
        }
    }
    
    /**
     * Agrega un registro de evento al CSV.
     * @param record Registro de evento
     */
    public void appendEvent(EventRecord record) {
        if (eventWriter != null) {
            EventRecord recordWithId = new EventRecord(
                eventId.getAndIncrement(),
                record.step(),
                record.eventName(),
                record.description(),
                record.affectedEntities(),
                record.timestamp()
            );
            eventWriter.println(recordWithId.toCsvLine());
            eventWriter.flush();
        }
    }
    
    @Override
    public void close() {
        if (revenueWriter != null) revenueWriter.close();
        if (expenseWriter != null) expenseWriter.close();
        if (eventWriter != null) eventWriter.close();
    }
}