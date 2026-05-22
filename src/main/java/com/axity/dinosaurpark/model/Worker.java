package com.axity.dinosaurpark.model;

public abstract class Worker {
    private final int id;
    private final String name;
    private final double dailySalary;
    
    public Worker(int id, String name, double dailySalary) {
        this.id = id;
        this.name = name;
        this.dailySalary = dailySalary;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getDailySalary() {
        return dailySalary;
    }
       
    /**
     * @return El rol del trabajador ("GUARD" o "TECHNICIAN")
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("%s{id=%d, name='%s', salary=%.2f}",
                getRole(), id, name, dailySalary);
    }
}
