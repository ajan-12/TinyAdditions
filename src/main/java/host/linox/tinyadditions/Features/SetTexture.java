package host.linox.tinyadditions.Features;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;
import net.minecraft.server.v1_14_R1.PlayerInventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class SetTexture extends Feature {

    public SetTexture(final JavaPlugin plugin) {
        super(FeatureType.SETTEXTURE);
        Arrays.stream(getType().getCommands()).forEach(command -> plugin.getCommand(command).setExecutor(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!isEnabled()) return true;

        //Checking if the sender is a Player and has the permission.
        if (sender instanceof Player) {
            if (!sender.hasPermission("tinyadditions.settexture.use")) {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
                return true;
            }

            //Checking the arguments.
            if (args.length != 1) return false;
            org.bukkit.inventory.ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            if (item.getType() != Material.PLAYER_HEAD && item.getType() != Material.PLAYER_WALL_HEAD) {
                sender.sendMessage(ChatColor.DARK_RED + "You aren't holding a player head!");
            }
            final PlayerInventory inv = ((CraftInventoryPlayer) ((Player) sender).getInventory()).getInventory();
            final ItemStack itemNMS = inv.getItemInHand();

            //Getting the texture url.
            final NBTTagCompound oldTag = itemNMS.getTag();
            if (oldTag == null) {
                sender.sendMessage(ChatColor.DARK_RED + "The player head you are holding does not have a skin!");
                return true;
            }
            final NBTTagCompound oldSkullOwner = oldTag.getCompound("SkullOwner");
            if (oldSkullOwner == null) {
                sender.sendMessage(ChatColor.DARK_RED + "The player head you are holding does not have a skin!");
                return true;
            }
            final NBTTagString name = (NBTTagString) oldSkullOwner.get("Name");
            if (name == null) {
                sender.sendMessage(ChatColor.DARK_RED + "The player head you are holding does not have a skin!");
                return true;
            }
            final NBTTagString uuid = (NBTTagString) oldSkullOwner.get("Id");
            if (uuid == null) {
                sender.sendMessage(ChatColor.DARK_RED + "The player head you are holding does not have a skin!");
                return true;
            }

            //Preparing the data tag of the item.
            final NBTTagCompound tag = new NBTTagCompound();
            final NBTTagCompound skullOwner = new NBTTagCompound();

            skullOwner.set("Id", uuid);
            skullOwner.set("Name", name);

            final NBTTagCompound properties = new NBTTagCompound();
            final NBTTagList textures = new NBTTagList();
            final NBTTagCompound texture = new NBTTagCompound();
            final NBTTagString value = new NBTTagString(args[0]);

            texture.set("Value", value);
            textures.add(0, texture);
            properties.set("textures", textures);
            skullOwner.set("Properties", properties);
            tag.set("SkullOwner", skullOwner);

            itemNMS.setTag(tag);

            //Processing the message.
            final ComponentBuilder message = new ComponentBuilder("");
            message.append(ChatColor.GREEN + "The texture of " + ChatColor.YELLOW + name.asString() + ChatColor.GREEN + " is now:");
            final TextComponent texturePart = new TextComponent(value.asString());
            texturePart.setClickEvent(new ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND, value.asString()
            ));
            texturePart.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
            message.append(texturePart);
            sender.spigot().sendMessage(message.create());
        } else sender.sendMessage("This command can only be applied in-game.");

        return true;
    }

}
