package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.simulation.ParkState;

public class ParkMonitor {
    
    public static void displaySnapshot(ParkState state) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    MONITOR DEL PARQUE                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Step: %-60d ║%n", state.getCurrentStep());
        System.out.printf("║ 1. Turistas activos: %-46d ║%n", state.countActiveTourists());
        System.out.printf("║ 2. Dinosaurios en encierro: %-38d ║%n", state.countDinosaursInEnclosure());
        System.out.printf("║ 3. Energía disponible: %-46.1f%% ║%n", state.getPowerPlant().getEnergyPercentage());
        System.out.printf("║ 4. Eventos activos: %-49s ║%n", 
            state.getActiveEventNames().isEmpty() ? "Ninguno" : String.join(", ", state.getActiveEventNames()));
        System.out.printf("║ 5. Vehículos no disponibles: %-40d ║%n", state.countVehiclesNotAvailable());
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
}