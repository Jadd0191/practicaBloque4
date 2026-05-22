package com.axity.dinosaurpark.simulation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Technician;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.persistence.CsvWriter;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

public class SimulationEngine {
    
    private final ParkConfig config;
    private final ParkState state;
    private final EventScheduler scheduler;
    private final int totalSteps;
    private final int arrivalBatchSize;
    
    public SimulationEngine(ParkConfig config) {
        this.config = config;
        this.totalSteps = config.getTotalSteps();
        this.arrivalBatchSize = config.getArrivalBatchSize();
        
        // Inicializar componentes
        CsvWriter csvWriter = new CsvWriter(config.getString("output.directory", "output"));
        
        ArrivalZone arrivalZone = new ArrivalZone();
        CentralHub centralHub = new CentralHub();
        BathroomZone bathroomZone = new BathroomZone();
        PowerPlant powerPlant = new PowerPlant();
        ObservationEnclosure basicEnclosure = new ObservationEnclosure("Encierro Básico", ExperienceType.BASIC);
        ObservationEnclosure premiumEnclosure = new ObservationEnclosure("Encierro Premium", ExperienceType.PREMIUM);
        ObservationEnclosure vipEnclosure = new ObservationEnclosure("Encierro VIP", ExperienceType.VIP);
        
        // Crear turistas
        List<Tourist> tourists = createTourists();
        for (Tourist t : tourists) {
            arrivalZone.addToQueue(t);
        }
        
        // Crear dinosaurios
        List<Dinosaur> dinosaurs = createDinosaurs();
        
        // Crear trabajadores
        List<Worker> workers = createWorkers();
        
        // Crear estado
        this.state = new ParkState(tourists, dinosaurs, workers, arrivalZone, centralHub,
            bathroomZone, powerPlant, basicEnclosure, premiumEnclosure, vipEnclosure,
            csvWriter, config.getSeed());
        
        // Crear scheduler
        this.scheduler = new EventScheduler(config.getSeed(), totalSteps);
    }
    
    private List<Tourist> createTourists() {
        List<Tourist> tourists = new ArrayList<>();
        int totalTourists = config.getTotalTourists();
        for (int i = 1; i <= totalTourists; i++) {
            tourists.add(new Tourist(i, "Turista_" + i));
        }
        return tourists;
    }
    
    private List<Dinosaur> createDinosaurs() {
        List<Dinosaur> dinosaurs = new ArrayList<>();
        int carnivoreCount = config.getCarnivoreCount();
        int herbivoreCount = config.getHerbivoreCount();
        
        for (int i = 1; i <= carnivoreCount; i++) {
            dinosaurs.add(new CarnivoreDinosaur(i, "Carnivoro_" + i, "T-Rex"));
        }
        for (int i = 1; i <= herbivoreCount; i++) {
            dinosaurs.add(new HerbivoreDinosaur(carnivoreCount + i, "Herbivoro_" + i, "Triceratops"));
        }
        return dinosaurs;
    }
    
    private List<Worker> createWorkers() {
        List<Worker> workers = new ArrayList<>();
        double dailySalary = config.getDouble("workers.dailySalary", 150.0);
        int guardCount = config.getInt("workers.guards", 3);
        int technicianCount = config.getInt("workers.technicians", 2);
        
        for (int i = 1; i <= guardCount; i++) {
            workers.add(new Guard(i, "Guardia_" + i, dailySalary));
        }
        for (int i = 1; i <= technicianCount; i++) {
            workers.add(new Technician(guardCount + i, "Tecnico_" + i, dailySalary));
        }
        return workers;
    }
    
    public void run() {
        System.out.println("=== INICIANDO SIMULACIÓN ===");
        final CsvWriter csvWriter = state.getCsvWriter();  // ← AGREGAR "final"
        
        for (int step = 1; step <= totalSteps; step++) {
            state.incrementStep();
            
            // Llegadas
            List<Tourist> arrived = state.getArrivalZone().processBatch(arrivalBatchSize, csvWriter);
            for (Tourist t : arrived) state.addRevenue(state.getArrivalZone().getTicketPrice());
            
            // Movimiento
            List<Tourist> active = state.getTourists().stream()
                .filter(t -> t.getStatus() == TouristStatus.IN_PARK).toList();
            Random rng = state.getRng();
            for (Tourist t : active) {
                state.getCentralHub().visit(t, rng, csvWriter);
                state.getBathroomZone().tryEnter(t, rng, csvWriter);
                int choice = rng.nextInt(3);
                ObservationEnclosure enclosure;
                switch (choice) {
                    case 0 -> enclosure = state.getBasicEnclosure();
                    case 1 -> enclosure = state.getPremiumEnclosure();
                    default -> enclosure = state.getVipEnclosure();
                }
                enclosure.visit(t, rng, csvWriter);
            }
            
            // Ticks
            state.getBathroomZone().tick();
            state.getPowerPlant().tick(rng, csvWriter);
            
            // Evento
            final long currentStep = step;  // ← variable final para usar en lambda
            scheduler.checkForEvent(step).ifPresent(e -> {
                System.out.println("[Step " + currentStep + "] EVENTO: " + e.getName());
                e.execute(state, rng);
            });
            
            // Workers
            for (Worker w : state.getWorkers()) {
                if (w instanceof Guard g) {
                    g.recaptureEscapedDinosaurs(state.getDinosaurs());
                } else if (w instanceof Technician t) {
                    t.repairIfNeeded(state.getPowerPlant());
                }
            }
            
            // Salarios cada 10 steps
            if (step % 10 == 0) {
                double salaries = state.getWorkers().stream().mapToDouble(Worker::getDailySalary).sum();
                state.addExpense(salaries);
                if (csvWriter != null) {
                    csvWriter.appendExpense(new ExpenseRecord(0, "SALARIES", salaries,
                        "Pago de salarios", LocalDateTime.now()));
                }
            }
            
            // Monitoreo
            ParkMonitor.displaySnapshot(state);
        }
        
        if (csvWriter != null) csvWriter.close();
        
        System.out.println("\n=== SIMULACIÓN FINALIZADA ===");
        System.out.println("Ingresos: $" + state.getTotalRevenue());
        System.out.println("Gastos: $" + state.getTotalExpenses());
        System.out.println("Beneficio: $" + (state.getTotalRevenue() - state.getTotalExpenses()));
    }

}