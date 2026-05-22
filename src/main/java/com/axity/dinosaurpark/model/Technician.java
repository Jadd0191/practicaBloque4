package com.axity.dinosaurpark.model;

public class Technician extends Worker {
    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }
    
    @Override
    public String getRole() {
        return "TECHNICIAN";
    }
    
    /**
     * Repara la planta de energía si no está operacional.
     * @param plant La planta de energía (se pasa como parámetro por ahora)
     */
    /*public void repairIfNeeded(PowerPlant plant) {
        if (plant == null) {
            return;
        }
        
        if (!plant.isOperational()) {
            plant.repair();
        }
    }*/
}
