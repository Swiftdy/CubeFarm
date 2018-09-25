package me.candyyn.cubefarm.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.manager.ChatManager;
import me.candyyn.cubefarm.manager.FarmManager;
import me.candyyn.cubefarm.manager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmCommand implements CommandExecutor {

    private final FarmManager farmManager;
    private final CubeFarm cubeFarm;
    private final WorldManager worldManager;
    private Logger log = Bukkit.getLogger(); // Use bukkit's built in logger

    public FarmCommand(CubeFarm cubeFarm) {
        this.worldManager = cubeFarm.getWorldManager();
        this.farmManager = cubeFarm.getFarmManager();
        this.cubeFarm = cubeFarm;
    }
    //No need to create a new one

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            log.info("Command not supported for console");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            // Not enough arguments
            return false;
        }
        // Covnert args[0] to lowercase to make up for arguments with capital letters such as "HELP"
        Farm farm = null;
        switch (args[0].toLowerCase()) {
            case "help":
                ChatManager.sendhelp(player);
                break;
            case "create":
                if (farmManager.hasFarm(player.getUniqueId())) {
                    ChatManager.sendmessage(player, "You already own a farm!", ChatColor.RED);
                    return false;
                }

                farm = farmManager.createFarm(player, worldManager);
                ChatManager.sendmessage(player, "Farm Created. Teleporting...", ChatColor.AQUA);
                farm.teleport(player);
                break;
            case "warp":
                if (!farmManager.hasFarm(player.getUniqueId())) {
                    // Player doesn't have a farm
                    return false;
                }
                farm = farmManager.getFarm(player.getUniqueId(), worldManager);
                farm.teleport(player);
            case "teleport":
            case "tp":
                String worldName = cubeFarm.getConfig().getString("world");
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    log.info(worldName + " could not be found. Please generate world.");
                    ChatManager.sendmessage(player,
                            "Unable to load world: " + worldName, ChatColor.RED);
                    return false;
                }
                player.teleport(world.getSpawnLocation());
                break;
            default:
                ChatManager.sendhelp(player);
                break;
        }
        return true;
    }
    public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args){
        List<String> tab = new ArrayList<String>();
        if(cmd.getName().equalsIgnoreCase("farm") && args.length >= 0){
            if(sender instanceof Player){
                Player player = (Player) sender;

                List<String> list = new ArrayList<>();
                tab.add("help");
                tab.add("teleport");
                tab.add("warp");
                tab.add("create");

                return tab;

            }
        }
        return tab;
    }
}