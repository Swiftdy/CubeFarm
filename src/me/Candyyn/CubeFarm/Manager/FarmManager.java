package me.Candyyn.CubeFarm.Manager;

import me.Candyyn.CubeFarm.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class FarmManager {
    // Boolean is a class, no need to create a new object
    public static boolean hasAFarm(Player player) {
        /* TODO: Create the Checker */
        String uuid = String.valueOf(player.getUniqueId());
        if(Main.getMain().getConfig().get("farms." + uuid) == null) {
            return false;
        }
        return true;
    }

    public static void createFarm(Player player) {
        if(!hasAFarm(player)) {
            String uuid = String.valueOf(player.getUniqueId());
            Main.getMain().getConfig().set("farms." + uuid + ".upgrade", 0);
            Main.getMain().getConfig().set("farms." + uuid + ".friends", "");
            Main.getMain().getConfig().set("farms." + uuid + ".banned", false);
            Main.getMain().getConfig().set("farms." + uuid + ".spawn", "0,0,0");
            Main.getMain().getConfig().set("farms." + uuid + ".island", "row::col");
            Main.getMain().saveConfig();
            ChatManager.sendmessage(player, "Farm Created", ChatColor.AQUA);

        }
    }


}
