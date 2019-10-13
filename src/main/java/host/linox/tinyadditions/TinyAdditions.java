package host.linox.tinyadditions;

import host.linox.tinyadditions.Data.DataManager;
import host.linox.tinyadditions.Data.DataStorage;
import host.linox.tinyadditions.Features.Feature;
import host.linox.tinyadditions.Features.ForceMessage;
import host.linox.tinyadditions.Features.ForceSudo;
import host.linox.tinyadditions.Features.GetTexture;
import host.linox.tinyadditions.Features.PermissionMessage;
import host.linox.tinyadditions.Features.SetTexture;
import host.linox.tinyadditions.Features.Spawn;
import host.linox.tinyadditions.Logging.DebugLevel;
import host.linox.tinyadditions.Logging.Logging;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class TinyAdditions extends JavaPlugin {

    private static TinyAdditions instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Initializing Logging.
        new Logging(this);

        // Initializing Data Storage.
        Logging.debug(DebugLevel.LOW_LEVEL, Level.INFO, "Initializing Data Storage.");
        new DataStorage();
        Logging.debug(DebugLevel.HIGH_LEVEL, Level.INFO, "Ensuring data file validity.");
        DataManager.ensureDataFile(this);

        // Initializing features.
        Logging.debug(DebugLevel.LOW_LEVEL, Level.INFO, "Initializing TinyAdditions features.");
        final ConfigurationSection features = getConfig().getConfigurationSection("settings.features");
        if (features == null) {
            Logging.log(Level.SEVERE, "Corrupted \"config.yml\" file!", "Recreating \"config.yml\" file.");

            final File configFile = new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml");
            configFile.delete();
            saveDefaultConfig();

            Logging.log(Level.INFO, "Created a new \"config.yml\" file.");
            return;
        }
        features.getKeys(false).forEach(key -> {

            Logging.debug(DebugLevel.HIGH_LEVEL, Level.INFO, "Initializing feature \"" + key + "\".");
            final boolean state = features.getBoolean(key, true);
            switch (key) {
                case "ForceMessage":
                    new ForceMessage(this).toggle(state);
                    break;
                case "PermissionMessage":
                    new PermissionMessage(this).toggle(state);
                    break;
                case "ForceSudo":
                    new ForceSudo(this).toggle(state);
                    break;
                case "GetTexture":
                    new GetTexture(this).toggle(state);
                    break;
                case "SetTexture":
                    new SetTexture(this).toggle(state);
                    break;
                case "Spawn":
                    new Spawn(this).toggle(state);
                    break;
                default:
                    return;
            }

            Logging.debug(DebugLevel.LOW_LEVEL, Level.INFO, (state ? "Enabling " : "Disabling ") + "the feature, \"" + key + "\".");
        });

        final Boolean autoSave = getConfig().getBoolean("settings.auto-save.enabled", true);
        final Long autoSaveInterval = getConfig().getLong("settings.auto-save.interval", 6000L);
        DataStorage.getInstance().setAutoSaveEnabled(autoSave);
        DataStorage.getInstance().setAutoSaveInterval(autoSaveInterval);

        Logging.log(Level.INFO, (autoSave ? "Starting" : "Not starting") + " auto-save with the interval of " + autoSaveInterval + " ticks.");
        if (autoSave) {
            final int autosave = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, DataManager::saveDataFile, autoSaveInterval, autoSaveInterval);
            DataStorage.getInstance().setAutoSaveID(autosave);
        }
    }

    @Override
    public void onDisable() {
        DataManager.saveDataFile();

        DataManager.dump();
        DataStorage.dump();
        Logging.dump();
        instance = null;
    }

    public static TinyAdditions getInstance() { return instance; }
}
