package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;

public class ParkMonitor {
    
    public static void displaySnapshot(ParkState state) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    MONITOR DEL PARQUE                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Step actual: %-50d ║%n", state.getCurrentStep());
        System.out.printf("║ Turistas activos: %-44d ║%n", state.countActiveTourists());
        System.out.printf("║ Dinosaurios en encierro: %-37d ║%n", state.countDinosaursInEnclosure());
        
        PowerPlant plant = state.getPowerPlant();
        double energyPercent = plant.getEnergyPercentage();
        System.out.printf("║ Energía disponible: %-43.1f%% ║%n", energyPercent);
        
        System.out.printf("║ Ingresos acumulados: $%-39.2f ║%n", state.getTotalRevenue());
        System.out.printf("║ Gastos acumulados: $%-40.2f ║%n", state.getTotalExpenses());
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}