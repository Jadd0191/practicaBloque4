package com.axity.dinosaurpark.simulation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.event.BlackoutEvent;
import com.axity.dinosaurpark.event.DealsHourEvent;
import com.axity.dinosaurpark.event.DinosaurEscapeEvent;
import com.axity.dinosaurpark.event.SimulationEvent;
import com.axity.dinosaurpark.event.StormEvent;
import com.axity.dinosaurpark.event.VehicleFailureEvent;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Technician;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Vehicle;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ExperienceType;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.PowerPlant;

public class SimulationEngine {
    
    private ParkState state;
    private final ParkConfig config;
    private final int totalSteps;
    private final int arrivalBatchSize;
    private final int monitoringInterval;
    private List<SimulationEvent> allEvents;
    
    public SimulationEngine(ParkConfig config) {
        this.config = config;
        this.totalSteps = config.getTotalSteps();
        this.arrivalBatchSize = config.getArrivalBatchSize();
        this.monitoringInterval = config.getInt("monitoring.intervalSteps", 10);
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
    
    private List<Vehicle> createVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        int vehicleCount = config.getInt("vehicles.count", 4);
        int repairSteps = config.getInt("vehicles.repairSteps", 5);
        
        for (int i = 1; i <= vehicleCount; i++) {
            vehicles.add(new Vehicle(i, "Vehiculo_" + i, repairSteps));
        }
        System.out.println("Creados " + vehicleCount + " vehículos de mantenimiento");
        return vehicles;
    }
    
    public void run() {
        DatabaseService db = null;
        
        try {
            // ============================================
            // 1. Inicializar persistencia (UNA SOLA VEZ)
            // ============================================
            String dbPath = config.getString("db.path", "./data/parkdb");
            db = new DatabaseService(dbPath);
            
            // ============================================
            // 2. Crear zonas
            // ============================================
            ArrivalZone arrivalZone = new ArrivalZone();
            CentralHub centralHub = new CentralHub();
            BathroomZone bathroomZone = new BathroomZone();
            PowerPlant powerPlant = new PowerPlant();
            ObservationEnclosure basicEnclosure = new ObservationEnclosure("Encierro Básico", ExperienceType.BASIC);
            ObservationEnclosure premiumEnclosure = new ObservationEnclosure("Encierro Premium", ExperienceType.PREMIUM);
            ObservationEnclosure vipEnclosure = new ObservationEnclosure("Encierro VIP", ExperienceType.VIP);
            
            // ============================================
            // 3. Crear turistas
            // ============================================
            List<Tourist> tourists = createTourists();
            for (Tourist t : tourists) {
                arrivalZone.addToQueue(t);
            }
            
            // ============================================
            // 4. Crear dinosaurios, trabajadores y vehículos
            // ============================================
            List<Dinosaur> dinosaurs = createDinosaurs();
            List<Worker> workers = createWorkers();
            List<Vehicle> vehicles = createVehicles();
            
            // ============================================
            // 5. Crear estado (con el db creado aquí)
            // ============================================
            this.state = new ParkState(tourists, dinosaurs, workers, vehicles,
                arrivalZone, centralHub, bathroomZone, powerPlant,
                basicEnclosure, premiumEnclosure, vipEnclosure,
                db, 0L);
            
            // ============================================
            // 6. Crear eventos con probabilidades
            // ============================================
            this.allEvents = Arrays.asList(
                new DinosaurEscapeEvent(config.getDouble("event.escape.probability", 0.05)),
                new BlackoutEvent(config.getDouble("event.blackout.probability", 0.03)),
                new StormEvent(config.getDouble("event.storm.probability", 0.04)),
                new DealsHourEvent(config.getDouble("event.deals.probability", 0.08)),
                new VehicleFailureEvent(config.getDouble("event.vehicleFailure.probability", 0.06))
            );
            
            // ============================================
            // 7. Loop de simulación
            // ============================================
            System.out.println("=== INICIANDO SIMULACIÓN - NIVEL INTERMEDIO ===");
            System.out.println("Total steps: " + totalSteps);
            System.out.println("Total turistas: " + config.getTotalTourists());
            System.out.println("Vehículos: " + state.getVehicles().size());
            System.out.println("Monitoreo cada: " + monitoringInterval + " steps");
            System.out.println("===============================================\n");
            
            Random rng = state.getRng();
            
            for (int step = 1; step <= totalSteps; step++) {
                state.incrementStep();
                state.clearActiveEvents();
                
                double currentDiscount = state.getCurrentDiscount();
                
                // A. Llegadas
                List<Tourist> arrived = state.getArrivalZone().processBatch(arrivalBatchSize, currentDiscount);
                for (Tourist t : arrived) {
                    double ticketPrice = state.getArrivalZone().getTicketPrice() * (1 - currentDiscount);
                    state.addRevenue(ticketPrice);
                    
                    RevenueRecord revenue = new RevenueRecord(
                        0, "TICKET", ticketPrice, t.getId(), 
                        state.getArrivalZone().getName(), LocalDateTime.now()
                    );
                    state.getDb().appendRevenue(revenue);
                }
                
                // B. Movimiento de turistas activos
                List<Tourist> activeTourists = state.getTourists().stream()
                    .filter(t -> t.getStatus() == TouristStatus.IN_PARK)
                    .toList();
                
                for (Tourist tourist : activeTourists) {
                    state.getCentralHub().visit(tourist, rng);
                    state.getBathroomZone().tryEnter(tourist, rng);
                    
                    int choice = rng.nextInt(3);
                    ObservationEnclosure enclosure = switch (choice) {
                        case 0 -> state.getBasicEnclosure();
                        case 1 -> state.getPremiumEnclosure();
                        default -> state.getVipEnclosure();
                    };
                    enclosure.visit(tourist, rng);
                }
                
                // C. Ticks
                state.getBathroomZone().tick();
                state.getPowerPlant().tick(rng);
                for (Vehicle v : state.getVehicles()) {
                    v.tick();
                }
                
                // D. Eventos probabilísticos
                for (SimulationEvent event : allEvents) {
                    double probability = 0.0;
                    if (event instanceof DinosaurEscapeEvent) {
                        probability = config.getDouble("event.escape.probability", 0.05);
                    } else if (event instanceof BlackoutEvent) {
                        probability = config.getDouble("event.blackout.probability", 0.03);
                    } else if (event instanceof StormEvent) {
                        probability = config.getDouble("event.storm.probability", 0.04);
                    } else if (event instanceof DealsHourEvent) {
                        probability = ((DealsHourEvent) event).getProbability();
                    } else if (event instanceof VehicleFailureEvent) {
                        probability = ((VehicleFailureEvent) event).getProbability();
                    }
                    
                    if (rng.nextDouble() < probability) {
                        System.out.println("[Step " + step + "] EVENTO: " + event.getName());
                        event.execute(state, rng);
                        state.addActiveEvent(event.getName());
                    }
                }
                
                // E. Workers
                for (Worker worker : state.getWorkers()) {
                    if (worker instanceof Guard guard) {
                        guard.recaptureEscapedDinosaurs(state.getDinosaurs());
                    } else if (worker instanceof Technician technician) {
                        technician.repairIfNeeded(state.getPowerPlant(), state.getVehicles());
                    }
                }
                
                // F. Salarios (cada 10 steps)
                if (step % 10 == 0) {
                    double totalSalaries = state.getWorkers().stream()
                        .mapToDouble(Worker::getDailySalary)
                        .sum();
                    state.addExpense(totalSalaries);
                    
                    ExpenseRecord expense = new ExpenseRecord(
                        0, "SALARIES", totalSalaries,
                        "Pago de salarios a trabajadores", LocalDateTime.now()
                    );
                    state.getDb().appendExpense(expense);
                }
                
                // G. Monitoreo (cada N steps)
                if (step % monitoringInterval == 0) {
                    ParkMonitor.displaySnapshot(state);
                }
            }
            
            // ============================================
            // 8. Resumen final
            // ============================================
            System.out.println("\n=== SIMULACIÓN FINALIZADA ===");
            System.out.println("Ingresos totales: $" + state.getTotalRevenue());
            System.out.println("Gastos totales: $" + state.getTotalExpenses());
            System.out.println("Beneficio neto: $" + (state.getTotalRevenue() - state.getTotalExpenses()));
            System.out.println("============================");
            
        } catch (Exception e) {
            System.err.println("ERROR durante la simulación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
                System.out.println("Conexión a base de datos cerrada (finally)");
            }
        }
    }
}