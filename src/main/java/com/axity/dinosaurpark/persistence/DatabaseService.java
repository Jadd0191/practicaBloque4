package com.axity.dinosaurpark.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class DatabaseService implements AutoCloseable {
    
    private Connection connection;
    
    public DatabaseService(String dbPath) {
        try {
            String url = "jdbc:h2:./" + dbPath;
            this.connection = DriverManager.getConnection(url, "sa", "");
            this.connection.setAutoCommit(true); // ← FORZAR AUTOCOMMIT
            runLiquibase();
            System.out.println("Base de datos H2 inicializada en: " + dbPath);
        } catch (SQLException e) {
            System.err.println("Error de conexión H2: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void runLiquibase() {
        try {
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                "db/changelog/db.changelog-master.xml",
                new ClassLoaderResourceAccessor(),
                database
            );
            liquibase.update("");
            System.out.println("Liquibase: Tablas creadas/actualizadas");
        } catch (Exception e) {
            System.err.println("Error en Liquibase: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void appendRevenue(RevenueRecord record) {
        if (connection == null) {
            System.err.println("ERROR: Connection es NULL");
            return;
        }
        
        String sql = "INSERT INTO revenues (type, amount, tourist_id, zone, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, record.type());
            stmt.setDouble(2, record.amount());
            stmt.setInt(3, record.touristId());
            stmt.setString(4, record.zone());
            stmt.setTimestamp(5, Timestamp.valueOf(record.timestamp()));
            int rows = stmt.executeUpdate();
            System.out.println("DB INSERT: Revenue - " + record.type() + " - $" + record.amount() + " (filas: " + rows + ")");
            
            // Forzar commit adicional (por si acaso)
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Error insertando revenue: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
        }
    }
    
    public void appendExpense(ExpenseRecord record) {
        if (connection == null) {
            System.err.println("ERROR: Connection es NULL");
            return;
        }
        
        String sql = "INSERT INTO expenses (type, amount, description, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, record.type());
            stmt.setDouble(2, record.amount());
            stmt.setString(3, record.description());
            stmt.setTimestamp(4, Timestamp.valueOf(record.timestamp()));
            int rows = stmt.executeUpdate();
            System.out.println("DB INSERT: Expense - " + record.type() + " - $" + record.amount() + " (filas: " + rows + ")");
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Error insertando expense: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
        }
    }
    
    public void appendEvent(EventRecord record) {
        if (connection == null) {
            System.err.println("ERROR: Connection es NULL");
            return;
        }
        
        String sql = "INSERT INTO events (step, event_name, description, affected_entities, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, record.step());
            stmt.setString(2, record.eventName());
            stmt.setString(3, record.description());
            stmt.setString(4, record.affectedEntities());
            stmt.setTimestamp(5, Timestamp.valueOf(record.timestamp()));
            int rows = stmt.executeUpdate();
            System.out.println("DB INSERT: Event - " + record.eventName() + " (filas: " + rows + ")");
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Error insertando event: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit(); // Commit final
                connection.close();
                System.out.println("Conexión H2 cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}