package com.axity.dinosaurpark.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {

    private static ParkConfig instance = null;
    private final Properties properties;

    private ParkConfig() {
        this.properties = new Properties();
        loadProperties();
    }

    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    private void loadProperties() {
        String propertyFile = "park.properties";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertyFile)) {
            
            if (input == null) {
                System.err.println("ERROR: No se pudo encontrar el archivo " + propertyFile);
                System.err.println("Asegúrate de que existe en: src/main/resources/park.properties");
                return;
            }
            
            properties.load(input);
            System.out.println("Configuración cargada correctamente desde: " + propertyFile);
            
        } catch (IOException e) {
            System.err.println("ERROR al cargar el archivo de configuración: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lee un valor entero del archivo de configuración.
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto si no existe la clave
     * @return Valor entero encontrado o el valor por defecto
     */
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir " + key + "=" + value + " a entero. Usando default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Lee un valor double del archivo de configuración.
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto si no existe la clave
     * @return Valor double encontrado o el valor por defecto
     */
    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir " + key + "=" + value + " a double. Usando default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Lee un valor String del archivo de configuración.
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto si no existe la clave
     * @return Valor String encontrado o el valor por defecto
     */
    public String getString(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return (value == null) ? defaultValue : value;
    }

    /**
     * Lee un valor boolean del archivo de configuración.
     * @param key Clave de la propiedad
     * @param defaultValue Valor por defecto si no existe la clave
     * @return Valor boolean encontrado o el valor por defecto
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * @return Semilla para números aleatorios (simulation.seed)
     */
    public long getSeed() {
        return getLong("simulation.seed", 42L);
    }

    /**
     * @return Número total de steps de la simulación (simulation.totalSteps)
     */
    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }

    /**
     * @return Tamaño del lote de llegada de turistas (simulation.arrivalBatchSize)
     */
    public int getArrivalBatchSize() {
        return getInt("simulation.arrivalBatchSize", 5);
    }

    /**
     * @return Número total de turistas (tourists)
     */
    public int getTotalTourists() {
        return getInt("tourists", 50);
    }

    /**
     * @return Número de dinosaurios carnívoros (dinosaurs.carnivores)
     */
    public int getCarnivoreCount() {
        return getInt("dinosaurs.carnivores", 5);
    }

    /**
     * @return Número de dinosaurios herbívoros (dinosaurs.herbivores)
     */
    public int getHerbivoreCount() {
        return getInt("dinosaurs.herbivores", 15);
    }

    /**
     * Lee un valor long del archivo de configuración.
     */
    public long getLong(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir " + key + "=" + value + " a long. Usando default: " + defaultValue);
            return defaultValue;
        }
    }

    static void resetForTesting() {
        instance = null;
    }
}