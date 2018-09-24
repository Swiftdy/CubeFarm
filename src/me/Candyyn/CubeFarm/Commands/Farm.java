package me.Candyyn.CubeFarm.Commands;

import me.Candyyn.CubeFarm.Main;
import me.Candyyn.CubeFarm.Manager.ChatManager;
import me.Candyyn.CubeFarm.Manager.FarmManager;
import me.Candyyn.CubeFarm.Manager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;


public class Farm implements CommandExecutor {

    private Logger log = Bukkit.getLogger(); // Use bukkit's built in logger
    private WorldManager worldManager;

    public Farm(WorldManager worldManager) {
        this.worldManager = worldManager;
    }
    //No need to create a new one

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length > 0) {
                switch (args[0]){
                    case "help" :
                        ChatManager.sendhelp(player);
                        break;
                    case "create" :
                        FarmManager.createFarm(player);
                        break;

                    case "teleport" :
                        String worldName = Main.getMain().getConfig().getString("world");
                        try {
                            player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
                        }catch (Exception e) {
                            log.info(Bukkit.getWorld(worldName).getName());
                            ChatManager.sendmessage(player, "Unable to load world: " + Bukkit.getWorld(worldName).getName(), ChatColor.RED);
                        }
                        break;

                    case "checkfree":
                        if(worldManager.getFarmgrid().isFree(0,0)) {
                            player.sendMessage("Its free");
                        }
                        break;

                    default:
                        ChatManager.sendhelp(player);
                        break;
                }

            }
            return true;
        }

        log.info("Command not supported for console");
        return true;
    }
}