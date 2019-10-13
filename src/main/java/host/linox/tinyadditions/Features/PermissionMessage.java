package host.linox.tinyadditions.Features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class PermissionMessage extends Feature {

    public PermissionMessage(final JavaPlugin plugin) {
        super(FeatureType.PERMISSIONMESSAGE);
        Arrays.stream(getType().getCommands()).forEach(command -> plugin.getCommand(command).setExecutor(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!isEnabled()) return true;

        //Checking if the sender is a Player and has permission for this command.
        if (sender instanceof Player) {
            if (!sender.hasPermission("tinyadditions.permissionedmessage.use")) {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
                return true;
            }
        }

        //Checking the arguments.
        if (args.length < 2) return false;

        //Processing the message.
        final StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]);
            if (i != (args.length - 1)) message.append(" ");
        }
        Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', message.toString()), args[0]);
        return true;
    }

}
