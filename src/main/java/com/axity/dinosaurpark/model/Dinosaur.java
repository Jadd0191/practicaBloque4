package com.axity.dinosaurpark.model;

public abstract class Dinosaur {
    private final int id;
    private final String name;
    private final String species;
    private DinosaurStatus status;
    private final double feedingCostPerDay;
    
    public Dinosaur(int id, String name, String species, double feedingCostPerDay) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.feedingCostPerDay = feedingCostPerDay;
        this.status = DinosaurStatus.IN_ENCLOSURE;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSpecies() {
        return species;
    }
    
    public DinosaurStatus getStatus() {
        return status;
    }
    
    public double getFeedingCostPerDay() {
        return feedingCostPerDay;
    }
    
    /**
     * El dinosaurio se escapa del recinto.
     * Solo puede escapar si está IN_ENCLOSURE.
     */
    public void escape() {
        if (this.status == DinosaurStatus.IN_ENCLOSURE) {
            this.status = DinosaurStatus.ESCAPED;
        }
    }
    
    /**
     * El dinosaurio es recapturado.
     * Cambia a estado RECAPTURED.
     */
    public void recapture() {
        if (this.status == DinosaurStatus.ESCAPED) {
            this.status = DinosaurStatus.RECAPTURED;
        }
    }
    
    /**
     * El dinosaurio regresa a su recinto.
     * Cambia de RECAPTURED a IN_ENCLOSURE.
     */
    public void returnToEnclosure() {
        if (this.status == DinosaurStatus.RECAPTURED) {
            this.status = DinosaurStatus.IN_ENCLOSURE;
        }
    }
    
    /**
     * @return La dieta del dinosaurio ("CARNIVORE" o "HERBIVORE")
     */
    public abstract String getDiet();
    
    /**
     * @return Nivel de peligro (0.0 a 1.0). Los carnívoros tienen peligro alto.
     */
    public abstract double getDangerLevel();
    
    @Override
    public String toString() {
        return String.format("Dinosaur{id=%d, name='%s', species='%s', status=%s, diet=%s, danger=%.1f}",
                id, name, species, status, getDiet(), getDangerLevel());
    }
}
