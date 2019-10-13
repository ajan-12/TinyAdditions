package host.linox.tinyadditions.Data;

import host.linox.tinyadditions.Logging.DebugLevel;
import host.linox.tinyadditions.Logging.Logging;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

public final class DataManager {

    private static File dataFile = null;

    /**
     * Static method for saving the data file.
     *
     * @return Whether the file is successfully saved.
     */
    public static boolean saveDataFile() {
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(dataFile);
        DataStorage.getInstance().getDataCells().forEach((feature, dataCell) -> dataCell.save(cfg));
        try {
            cfg.save(dataFile);
        } catch (Exception x) {
            Logging.logException(x, "Could not save the plugin data.");
        }
        return true;
    }

    /**
     * Static method for ensuring if the data file is existent.
     *
     * @param plugin An instance of Plugin.
     * @return Whether the file is found/created.
     */
    public static boolean ensureDataFile(final Plugin plugin) {
        dataFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data-storage.yml");

        try {
            if (!dataFile.exists()) {
                Logging.log(Level.INFO, "Could not find \"data-storage.yml\" file.", "Attempting to create a new file!");
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
                Logging.log(Level.INFO, "Successfully created a new \"data-storage.yml\" file!");
            }
            return true;
        } catch (Exception x) {
            Logging.logException(x, "An exception occurred while trying to create the file", "\"data-storage.json\".");
            return false;
        }
    }

    /**
     * Static getter for the data file.
     *
     * @return The data file. Might be null, \n
     *         if the data file could not be created.
     */
    public static File getDataFile() { return dataFile; }

    public static void dump() {
        Logging.debug(DebugLevel.HIGH_LEVEL, Level.INFO, "Dumping DataManager fields.");
        dataFile = null;
    }
}
