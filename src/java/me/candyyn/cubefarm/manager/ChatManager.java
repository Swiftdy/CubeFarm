package java.me.candyyn.cubefarm.manager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager {

    public static void sendmessage(Player player, String message, ChatColor color) {
        player.sendMessage(ChatColor.GREEN + "[Cubefarm] " +  color + message);
    }
    public static void sendhelp(Player player) {
        player.sendMessage("");
        player.sendMessage(" 》》 " +ChatColor.GREEN+"Help" +ChatColor.GRAY+ " Commands");
        player.sendMessage(" 》 /farm " +ChatColor.GRAY+" - Brings up the help menu");
        player.sendMessage(" 》 /farm home " +ChatColor.GRAY+" - Teleport you to your farm");
        player.sendMessage(" 》 /farm visit " +ChatColor.GRAY+" - visit a player");
        player.sendMessage(" 》 /farm shop " +ChatColor.GRAY+" - teleport you to the farm shop");
        player.sendMessage(" 》 /farm create " +ChatColor.GRAY+" - create's a farm");
        player.sendMessage("");
    }

}
