package host.linox.tinyadditions.Features;

import host.linox.tinyadditions.Data.DataStorage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetSpawn extends Feature {

    SetSpawn(final JavaPlugin plugin) {
        super(FeatureType.SPAWN);
        plugin.getCommand("setspawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!isEnabled()) return true;

        //Checking the arguments.
        if (args.length < 2) return false;

        final World targetWorld = Bukkit.getWorld(args[0]);
        if (targetWorld == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Invalid target world.");
            return true;
        }

        final World sourceWorld = Bukkit.getWorld(args[1]);
        if (sourceWorld == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Invalid source world.");
            return true;
        }

        final Location spawnLoc;
        if (args.length == 7 || args.length == 5) {
            for (int i = 2; i < args.length; i++) {
                if (!args[i].matches("(\\d++)(\\.\\d++)?")) {
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid coordinates. Argument: " + (i + 1));
                    return true;
                }
            }

            spawnLoc = args.length == 7 ? new Location(targetWorld,
                    Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]),
                    Float.parseFloat(args[5]), Float.parseFloat(args[6])
            ) : new Location(targetWorld,
                    Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));

        } else if (args.length == 2) {
            //Checking if the sender is a Player and has the permission.
            if (sender instanceof Player) {
                if (!sender.hasPermission("tinyadditions.spawn.set")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
                    return true;
                }
            } else {
                sender.sendMessage("This command can only be applied ingame!");
                return true;
            }

            spawnLoc = ((Player) sender).getLocation();
        } else return false;

        //Saving the spawn location.
        if (!DataStorage.getInstance().getDataCells().containsKey(FeatureType.SPAWN)) {
            sender.sendMessage(ChatColor.DARK_RED + "Could not set spawn.");
            return true;
        }

        final Spawn feature = ((Spawn) DataStorage.getInstance().getDataCells().get(FeatureType.SPAWN));
        if (feature.getSpawnEntries().containsKey(args[1])) feature.getSpawnEntries().replace(args[1], spawnLoc);
        else feature.getSpawnEntries().put(args[1], spawnLoc);

        sender.sendMessage(ChatColor.GREEN + "Successfully set the spawn location for " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + " from " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + ".");
        return true;
    }

}
