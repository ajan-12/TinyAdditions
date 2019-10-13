package host.linox.tinyadditions.Features;

import host.linox.tinyadditions.Data.DataStorage;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;
import net.minecraft.server.v1_14_R1.PlayerInventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class GetTexture extends Feature {

    public GetTexture(final JavaPlugin plugin) {
        super(FeatureType.GETTEXTURE);
        Arrays.stream(getType().getCommands()).forEach(command -> plugin.getCommand(command).setExecutor(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!isEnabled()) return true;

        //Checking if the player has the permission.
        if (!sender.hasPermission("tinyadditions.gettexture.use")) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
            return true;
        }

        //Checking the arguments.
        final Player target;
        final ItemStack itemNMS;
        if (args.length == 2) {
            HashMap<String, UUID> players = DataStorage.getInstance().getPlayers();
            if (players.containsKey(args[0])) target = Bukkit.getPlayer(players.get(args[0]));
            else {
                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.DARK_RED + "The target player is not found.");
                    return true;
                }
                players.put(args[0], target.getUniqueId());
            }
            if (!args[1].matches("[1-9]")) {
                sender.sendMessage(ChatColor.DARK_RED + "The target slot doesn't exist. Please use a number between 1 and 9.");
                return true;
            }
            org.bukkit.inventory.ItemStack item = (target.getInventory()).getItem(Integer.parseInt(args[1]));
            if (item == null || (item.getType() != Material.PLAYER_HEAD && item.getType() != Material.PLAYER_WALL_HEAD)) {
                sender.sendMessage(ChatColor.DARK_RED + "Target player doesn't have a player head in that slot.");
                return true;
            }
            final PlayerInventory inv = ((CraftInventoryPlayer) (target).getInventory()).getInventory();
            itemNMS = inv.getItem(Integer.parseInt(args[1]));

        } else if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be applied in-game.");
                return true;
            }
            target = (Player) sender;

            org.bukkit.inventory.ItemStack item = target.getInventory().getItemInMainHand();
            if (item.getType() != Material.PLAYER_HEAD && item.getType() != Material.PLAYER_WALL_HEAD) {
                sender.sendMessage(ChatColor.DARK_RED + "You aren't holding a player head!");
            }
            final PlayerInventory inv = ((CraftInventoryPlayer) (target).getInventory()).getInventory();
            itemNMS = inv.getItemInHand();

        } else return false;

        //Getting the texture url.
        final NBTTagCompound tag = itemNMS.getTag();
        if (tag == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player head does not have a skin!");
            return true;
        }
        final NBTTagCompound skullOwner = tag.getCompound("SkullOwner");
        if (skullOwner == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player head does not have a skin!");
            return true;
        }
        final NBTTagString name = (NBTTagString) skullOwner.get("Name");
        if (name == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player head does not have a skin!");
            return true;
        }
        final NBTTagCompound properties = skullOwner.getCompound("Properties");
        if (properties == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player head does not have a skin!");
            return true;
        }
        final NBTTagList textures = properties.getList("textures", 10);
        if (textures == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player head does not have a skin!");
            return true;
        }
        final NBTTagCompound texture = textures.getCompound(0);
        if (texture == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player head does not have a skin!");
            return true;
        }
        final NBTTagString value = (NBTTagString) texture.get("Value");
        if (value == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player head does not have a skin!");
            return true;
        }

        //Processing the message.
        sender.sendMessage(ChatColor.GREEN + "The texture of " + ChatColor.YELLOW + name.asString() + ChatColor.GREEN + ":");
        final TextComponent texturePart = new TextComponent(value.asString());
        texturePart.setClickEvent(new ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND, value.asString()
        ));
        texturePart.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
        sender.spigot().sendMessage(texturePart);
        return true;
    }

}
