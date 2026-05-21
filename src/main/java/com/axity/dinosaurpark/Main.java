package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Parque Turístico de Dinosaurios");
        System.out.println("Probando ParkConfig (Singleton)");
        System.out.println("==========================================");
        
        // Obtener la instancia única de configuración
        ParkConfig config = ParkConfig.getInstance();
        
        // Leer algunos valores del archivo properties
        System.out.println("\n--- Configuración cargada ---");
        System.out.println("Semilla (seed): " + config.getSeed());
        System.out.println("Total steps: " + config.getTotalSteps());
        System.out.println("Total turistas: " + config.getTotalTourists());
        System.out.println("Dinosaurios carnívoros: " + config.getCarnivoreCount());
        System.out.println("Dinosaurios herbívoros: " + config.getHerbivoreCount());
        System.out.println("Precio del boleto: $" + config.getDouble("arrival.ticketPrice", 0.0));
        System.out.println("------------------------------");
        
        // Verificar que es Singleton (misma instancia)
        ParkConfig config2 = ParkConfig.getInstance();
        System.out.println("\n¿Misma instancia? " + (config == config2));
        System.out.println("Singleton funciona correctamente"); 
    }
}