package host.linox.tinyadditions.Features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class ForceSudo extends Feature {

    public ForceSudo (final JavaPlugin plugin) {
        super(FeatureType.FORCESUDO);
        Arrays.stream(getType().getCommands()).forEach(command -> plugin.getCommand(command).setExecutor(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!isEnabled()) return true;

        //Checking if the sender is a Player and has permission for this command.
        if (sender instanceof Player) {
            if (sender.hasPermission("tinyadditions.forcesudo.use")) {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
                return true;
            }
        }

        //Checking the arguments.
        if (args.length < 2) return false;
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return false;

        //Processing the sudo command.
        final StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]);
            if (i != (args.length - 1)) message.append(" ");
        }
        target.performCommand(ChatColor.translateAlternateColorCodes('&', message.toString()));
        return true;
    }

}
