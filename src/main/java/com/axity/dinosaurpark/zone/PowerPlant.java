package com.axity.dinosaurpark.zone;

import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.config.ParkConfig;

import java.time.LocalDateTime;
import java.util.Random;

public class PowerPlant {
    
    private final String name;
    private double currentEnergy;
    private final double initialEnergy;
    private final double consumptionPerStep;
    private final double failureProbability;
    private final double maintenanceCost;
    private final double repairCost;
    private boolean operational;
    
    public PowerPlant() {
        ParkConfig config = ParkConfig.getInstance();
        this.name = "Planta de Energía";
        this.initialEnergy = config.getDouble("powerplant.initialEnergy", 100.0);
        this.currentEnergy = initialEnergy;
        this.consumptionPerStep = config.getDouble("powerplant.consumptionPerStep", 1.5);
        this.failureProbability = config.getDouble("powerplant.failureProbability", 0.05);
        this.maintenanceCost = config.getDouble("powerplant.maintenanceCost", 200.0);
        this.repairCost = config.getDouble("powerplant.repairCost", 500.0);
        this.operational = true;
    }
    
    public String getName() {
        return name;
    }
    
    public double getCurrentEnergy() {
        return currentEnergy;
    }
    
    public double getEnergyPercentage() {
        return (currentEnergy / initialEnergy) * 100;
    }
    
    public boolean isOperational() {
        return operational;
    }
    
    /**
     * Avanza el tiempo: consume energía y verifica si ocurre una falla.
     * @param rng Generador de números aleatorios
     * @param csvWriter Para registrar gastos
     */
    public void tick(Random rng, CsvWriter csvWriter) {
        if (!operational) {
            // Si no está operacional, no consume energía
            return;
        }
        
        // Consumir energía
        currentEnergy -= consumptionPerStep;
        if (currentEnergy <= 0) {
            currentEnergy = 0;
            triggerFailure(csvWriter);
            return;
        }
        
        // Verificar falla aleatoria
        if (rng.nextDouble() < failureProbability) {
            triggerFailure(csvWriter);
        }
    }
    
    /**
     * Dispara una falla en la planta.
     * @param csvWriter Para registrar gastos
     */
    public void triggerFailure(CsvWriter csvWriter) {
        if (!operational) return;
        
        operational = false;
        currentEnergy = 0;
        
        // Registrar gasto de mantenimiento
        if (csvWriter != null) {
            ExpenseRecord expense = new ExpenseRecord(
                0,
                "MAINTENANCE",
                maintenanceCost,
                "Falla en planta de energía",
                LocalDateTime.now()
            );
            csvWriter.appendExpense(expense);
        }
    }
    
    public void repair() {
        if (operational) return;
        
        operational = true;
        currentEnergy = initialEnergy;
    }
    
    /**
     * Repara la planta y registra el gasto.
     * @param csvWriter Para registrar gastos
     */
    public void repair(CsvWriter csvWriter) {
        if (operational) return;
        
        operational = true;
        currentEnergy = initialEnergy;
        
        if (csvWriter != null) {
            ExpenseRecord expense = new ExpenseRecord(
                0,
                "REPAIR",
                repairCost,
                "Reparación de planta de energía",
                LocalDateTime.now()
            );
            csvWriter.appendExpense(expense);
        }
    }
}