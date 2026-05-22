package com.axity.dinosaurpark.model;

import java.util.List;

public class Guard extends Worker {
    public Guard(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }
    
    @Override
    public String getRole() {
        return "GUARD";
    }
    
    /**
     * Recaptura todos los dinosaurios que están en estado ESCAPED.
     * @param dinosaurs Lista de todos los dinosaurios del parque
     */
    public void recaptureEscapedDinosaurs(List<Dinosaur> dinosaurs) {
        if (dinosaurs == null) {
            return;
        }
        
        for (Dinosaur d : dinosaurs) {
            if (d.getStatus() == DinosaurStatus.ESCAPED) {
                d.recapture();
            }
        }
    }
    
    /**
     * Devuelve al recinto inmediatamente.
     * @param dinosaurs Lista de todos los dinosaurios del parque
     */
    public void recaptureAndReturnToEnclosure(List<Dinosaur> dinosaurs) {
        if (dinosaurs == null) {
            return;
        }
        
        for (Dinosaur d : dinosaurs) {
            if (d.getStatus() == DinosaurStatus.ESCAPED) {
                d.recapture();
                d.returnToEnclosure();
            }
        }
    }
}
