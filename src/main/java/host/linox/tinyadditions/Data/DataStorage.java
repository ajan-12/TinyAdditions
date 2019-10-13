package host.linox.tinyadditions.Data;

import host.linox.tinyadditions.Features.Feature;
import host.linox.tinyadditions.Features.FeatureType;
import host.linox.tinyadditions.Logging.DebugLevel;
import host.linox.tinyadditions.Logging.Logging;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public final class DataStorage {

    // Singleton Implementation
    private static DataStorage instance = null;

    public DataStorage() { instance = this; }

    public static DataStorage getInstance() { return instance; }
    public static void dump() {
        Logging.debug(DebugLevel.HIGH_LEVEL, Level.INFO, "Dumping DataStorage fields.");
        instance = null;
    }

    // Caching
    private HashMap<String, UUID> players = new HashMap<>();
    public HashMap<String, UUID> getPlayers() { return players; }

    // Data Cells
    private HashMap<FeatureType, DataCell> dataCells = new HashMap<>();
    public HashMap<FeatureType, DataCell> getDataCells() { return dataCells; }

    // Features
    private HashMap<FeatureType, Feature> features = new HashMap<>();
    public HashMap<FeatureType, Feature> getFeatures() { return features; }

    // Auto Save
    private int autoSaveID = Integer.MAX_VALUE;
    public int getAutoSaveID() { return autoSaveID; }
    public void setAutoSaveID(int autoSaveID) { this.autoSaveID = autoSaveID; }

    private Boolean autoSaveEnabled = true;
    public Boolean getAutoSaveEnabled() { return autoSaveEnabled; }
    public void setAutoSaveEnabled(final Boolean autoSaveEnabled) { this.autoSaveEnabled = autoSaveEnabled; }

    private Long autoSaveInterval = 6000L;
    public Long getAutoSaveInterval() { return autoSaveInterval; }
    public void setAutoSaveInterval(final Long autoSaveInterval) { this.autoSaveInterval = autoSaveInterval; }
}
