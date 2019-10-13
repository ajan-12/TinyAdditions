package host.linox.tinyadditions.Features;

import host.linox.tinyadditions.Data.DataCell;
import host.linox.tinyadditions.Data.DataManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Spawn extends DataCell {

    private boolean useWorldSpawn;
    private HashMap<String, Location> spawnEntries;

    public Spawn(final JavaPlugin plugin) {
        super(FeatureType.SPAWN);

        this.useWorldSpawn = plugin.getConfig().getBoolean("settings.spawn-feature.world-spawn-default", false);
        spawnEntries = new HashMap<>();
        load(YamlConfiguration.loadConfiguration(DataManager.getDataFile()));

        plugin.getCommand("spawn").setExecutor(this);

        new SetSpawn(plugin).toggle(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!isEnabled()) return true;

        //Checking if the sender is a Player.
        if (sender instanceof Player) {
            if (!sender.hasPermission("tinyadditions.spawn.use")) {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
                return true;
            }

            if (args.length != 0) return false;

            final String from = ((Player) sender).getWorld().getName();
            if (spawnEntries.containsKey(from)) {
                ((Player) sender).teleport(spawnEntries.get(from));
                return true;
            }

            if (useWorldSpawn)
                ((Player) sender).teleport(((Player) sender).getLocation().getWorld().getSpawnLocation());
            else sender.sendMessage(ChatColor.DARK_RED + "Could not find a set spawn.");

        } else sender.sendMessage("This command can only be applied ingame!");
        return true;
    }

    @Override
    public void load(final FileConfiguration cfg) {
        if (!spawnEntries.isEmpty()) spawnEntries = new HashMap<>();

        final ConfigurationSection dataSection;
        if (cfg.contains("SpawnFeature")) {
            dataSection = cfg.getConfigurationSection("SpawnFeature");
        } else {
            cfg.createSection("SpawnFeature");
            return;
        }

        for (final String from : dataSection.getKeys(false)) {
            final String rawTo = dataSection.getString(from);
            if (rawTo == null) continue;
            final String[] partsTo = rawTo.split(":");
            final Location to = new Location(
                    Bukkit.getWorld(partsTo[0]),
                    Double.parseDouble(partsTo[1]),
                    Double.parseDouble(partsTo[2]),
                    Double.parseDouble(partsTo[3]),
                    Float.parseFloat(partsTo[4]),
                    Float.parseFloat(partsTo[5])
            );
            spawnEntries.put(from, to);
        }
    }

    @Override
    public void save(final FileConfiguration cfg) {
        if (!spawnEntries.isEmpty()) {
            final ConfigurationSection dataSection;
            if (cfg.contains("SpawnFeature")) {
                dataSection = cfg.getConfigurationSection("SpawnFeature");
            } else {
                dataSection = cfg.createSection("SpawnFeature");
            }

            for (final Map.Entry<String, Location> entry : spawnEntries.entrySet()) {
                final Location to = entry.getValue();
                dataSection.set(entry.getKey(), to.getWorld().getName() + ":" + to.getX() + ":" + to.getY() + ":" + to.getZ() + ":" + to.getYaw() + ":" + to.getPitch());
            }
        }
    }

    HashMap<String, Location> getSpawnEntries() { return spawnEntries; }
}
