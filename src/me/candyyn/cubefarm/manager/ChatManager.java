package me.candyyn.cubefarm.manager;

import me.candyyn.cubefarm.utils.HelpType;
import org.bukkit.ChatColor;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;

public class ChatManager {

    // [Cubefarm] [CubeFarm] [
    public static void sendMessage(Player player, String message, ChatColor color) {
        player.sendMessage(ChatColor.GREEN + "[CubeFarm] " +  color + message);
    }

    public static void sendHelp(Player player, HelpType type) {
        switch(type) {
            case GENERAL:
                player.sendMessage("");
                player.sendMessage(" 》》 " +ChatColor.GREEN+"Help" +ChatColor.GRAY+ " Commands");
                player.sendMessage(" 》 /farm " +ChatColor.GRAY+" - Brings up the help menu");
                player.sendMessage(" 》 /farm home " +ChatColor.GRAY+" - Teleport you to your farm");
                player.sendMessage(" 》 /farm visit " +ChatColor.GRAY+" - visit a player");
                player.sendMessage(" 》 /farm shop " +ChatColor.GRAY+" - teleport you to the farm shop");
                player.sendMessage(" 》 /farm create " +ChatColor.GRAY+" - create's a farm");
                player.sendMessage("");
                break;
            case SCHEMATIC:
                player.sendMessage("");
                player.sendMessage(" 》》 " +ChatColor.GREEN+"Schematic Help" +ChatColor.GRAY+ " Commands");
                player.sendMessage(" 》 /farm schematic " +ChatColor.GRAY+" - Brings up the help menu");
                player.sendMessage(" 》 /farm schematic creategroup <name>" +ChatColor.GRAY+" - creates a schematic group");
                player.sendMessage(" 》 /farm schematic createslot <group> " +ChatColor.GRAY+" - creates a new schematic slot");
                player.sendMessage(" 》 /farm schematic deleteslot <group> <index> " +ChatColor.GRAY+" - deletes a schematic slot");
                player.sendMessage(" 》 /farm schematic deletegroup <name> " +ChatColor.GRAY+" - deletes a group");
                player.sendMessage(" 》 /farm schematic listgroups" +ChatColor.GRAY+" - lists all groups");
                player.sendMessage(" 》 /farm schematic listslots <group> " +ChatColor.GRAY+" - lists all slots in a group");
                player.sendMessage("");
                break;
            
        }
    }


}
