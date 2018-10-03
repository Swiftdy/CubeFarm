package me.candyyn.cubefarm.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.boydti.fawe.object.schematic.Schematic;
import com.mojang.datafixers.schemas.Schema;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.manager.ChatManager;
import me.candyyn.cubefarm.manager.FarmManager;
import me.candyyn.cubefarm.manager.SchematicManager;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.manager.playerdata.PlayerData;
import me.candyyn.cubefarm.manager.playerdata.PlayerDataManager;
import me.candyyn.cubefarm.schematic.FarmSchematic;
import me.candyyn.cubefarm.schematic.SchematicGrid;
import me.candyyn.cubefarm.utils.HelpType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmCommand implements CommandExecutor {

    //private final FarmManager farmManager;
    private final CubeFarm cubeFarm;
    private final WorldManager worldManager;
    private FarmGrid farmGrid;
    private final SchematicGrid schematicGrid;
    private final SchematicManager schematicManager;
    private final FarmManager farmManager;
    private final PlayerDataManager playerDataManager;
    private Logger log = Bukkit.getLogger();

    public FarmCommand(CubeFarm cubeFarm, WorldManager worldManager, FarmGrid farmGrid, SchematicGrid schematicGrid, SchematicManager schematicManager, FarmManager farmManager, PlayerDataManager playerDataManager) {
        this.worldManager = worldManager;
        this.farmGrid = farmGrid;
        this.cubeFarm = cubeFarm;
        this.schematicGrid = schematicGrid;
        this.schematicManager = schematicManager;
        this.farmManager = farmManager;
        this.playerDataManager = playerDataManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            log.info("Command not supported for console");
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = playerDataManager.getPlayerData(player.getUniqueId());
        if (args.length == 0) {
            ChatManager.sendHelp(player, HelpType.GENERAL);
            return true;
        }
        // Covnert args[0] to lowercase to make up for arguments with capital letters such as "HELP"
        Farm farm = null;
        switch (args[0].toLowerCase()) {
            case "help":
                if(args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "schematic":
                            ChatManager.sendHelp(player, HelpType.SCHEMATIC);
                            break;
                        default:
                            ChatManager.sendHelp(player, HelpType.GENERAL);
                            break;
                    }
                }else {
                    ChatManager.sendHelp(player, HelpType.GENERAL);
                }
                break;
            case "create":
                if(!player.hasPermission("cubefarm.create")) {
                    ChatManager.sendMessage(player, "You don't have permissions to create a farm", ChatColor.RED);
                    break;
                }
                if (farmGrid.hasFarm(player.getUniqueId())) {
                    ChatManager.sendMessage(player, "You already own a farm!", ChatColor.RED);
                    break;
                }
                playerData.setFarmIndex(cubeFarm.getConfig().getInt("farmamount"));
                farm = farmGrid.createFarm(playerData);
                farmManager.saveFarm(farm);
                ChatManager.sendMessage(player, "Creating Farm...", ChatColor.AQUA);
                farm.buildSections();
                playerDataManager.savePlayerData(player.getUniqueId());
                break;
            case "warp":
            case "home":
                if (!farmGrid.hasFarm(player.getUniqueId())) {
                    ChatManager.sendMessage(player, "You don't have a farm, type /farm create to make one.", ChatColor.RED);
                    break;
                }

                if(!farmGrid.getFarm(player.getUniqueId()).getBuilding()) {

                farm = farmGrid.getFarm(player.getUniqueId());
                if(!farm.getBuilding()) {
                    farm.teleport(player);
                }

                } else {
                    ChatManager.sendMessage(player, "Your farm is currently being generated", ChatColor.RED);
                }
                break;
            case "teleport":
            case "tp":
                String worldName = cubeFarm.getConfig().getString("world");
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    log.info(worldName + " could not be found. Please generate world.");
                    ChatManager.sendMessage(player,
                            "Unable to load world: " + worldName, ChatColor.RED);
                    break;
                }
                player.teleport(world.getSpawnLocation());
                break;
            case "delete":
                if(!player.hasPermission("cubefarm.delete")) {
                    ChatManager.sendMessage(player, "You don't have permissions to delete a farm.", ChatColor.RED);
                    break;
                }
                Farm d_farm = farmGrid.getFarm(player.getUniqueId());
                if(d_farm == null) {
                    ChatManager.sendMessage(player, "You must create a farm before deleting it.", ChatColor.RED);
                    break;
                }
                if(!d_farm.getBuilding()) {
                    ChatManager.sendMessage(player, "Deleting your personal farm and teleporting you to spawn...", ChatColor.RED);
                    farmGrid.getFarm(player.getUniqueId()).clear(); // Delete blocks
                    farmGrid.deleteFarm(player.getUniqueId()); // Remove data
                    farmManager.deleteFarm(playerData); // Set new data
                    playerDataManager.savePlayersData(); // Save player data
                    player.teleport(worldManager.getWorld().getSpawnLocation());
                } else {
                    ChatManager.sendMessage(player, "Your farm is currently being generated", ChatColor.RED);
                }
                break;
            case "schematic": //TODO: Link the call funktions to use schematics
                    if(!player.hasPermission("cubefarm.schematic")) {
                        ChatManager.sendMessage(player, "You don't have permissions to access the Schematic", ChatColor.RED);
                         break;
                    }
                    if(args.length == 1) {
                        ChatManager.sendHelp(player, HelpType.SCHEMATIC);
                        break;
                    }

                    switch (args[1].toLowerCase()) {
                        case "creategroup":
                            if(!player.hasPermission("cubefarm.schematic.delete")) {
                                ChatManager.sendMessage(player, "You don't have permissions to create a Schematic group", ChatColor.RED);
                                break;
                            }
                            if(args.length == 3) {
                                if(schematicGrid.checkGroup(args[2].toLowerCase())) {
                                    ChatManager.sendMessage(player, "A group with the name already exists", ChatColor.RED);
                                    break;
                                }else {
                                    schematicGrid.createGroup(args[2].toLowerCase());
                                    ChatManager.sendMessage(player, "Creating a new group called: " + args[2].toLowerCase(), ChatColor.BLUE);
                                    break;
                                }
                            }else {
                                ChatManager.sendHelp(player, HelpType.SCHEMATIC);
                            }
                            break;

                        case "create":
                            if(!player.hasPermission("cubefarm.schematic.delete")) {
                                ChatManager.sendMessage(player, "You don't have permissions to create a Schematic group or slot", ChatColor.RED);
                                break;
                            }
                            ChatManager.sendMessage(player, "you might mean createslot or creategroup?", ChatColor.BLUE);
                            break;
                        case "createslot":
                            if(!player.hasPermission("cubefarm.schematic.delete")) {
                                ChatManager.sendMessage(player, "You don't have permissions to create a Schematic slot", ChatColor.RED);
                                break;
                            }
                            if(args.length == 3) {
                                if(schematicGrid.checkGroup(args[2].toLowerCase())) {
                                    schematicGrid.createSchematic(args[2].toLowerCase());
                                    ChatManager.sendMessage(player, "Creating a new slot at: " + args[2].toLowerCase(), ChatColor.BLUE);
                                    break;
                                } else {
                                    ChatManager.sendMessage(player, "A group with the name doesn't exist", ChatColor.RED);
                                    break;
                                }
                            } else {
                                ChatManager.sendHelp(player, HelpType.SCHEMATIC);
                            }
                            break;
                        case "delete": //TODO: MAKE SAFER deleteslot, delete group
                            if(!player.hasPermission("cubefarm.schematic.delete")) {
                                ChatManager.sendMessage(player, "You don't have permissions to delete a Schematic group or slot", ChatColor.RED);
                                break;
                            }
                            if(args.length == 3 ) {
                                    schematicGrid.clearGroup(args[2].toLowerCase());
                                    schematicGrid.deleteGroup(args[2].toLowerCase());
                                        ChatManager.sendMessage(player, "Deleting group " + args[2].toLowerCase(), ChatColor.RED);
                                    player.teleport(worldManager.getWorld().getSpawnLocation());
                                    break;
                            } else if(args.length == 4) {

                                int index;
                                try {
                                    index = Integer.parseInt(args[3]);
                                }catch (Exception e) {
                                    ChatManager.sendHelp(player, HelpType.SCHEMATIC);
                                    break;
                                }
                                schematicGrid.getSlot(args[2].toLowerCase(), index).clear();
                                schematicGrid.deleteSlot(args[2].toLowerCase(), index);
                                
                                ChatManager.sendMessage(player, "Deleting group " + args[2].toLowerCase() + "'s slot " + index, ChatColor.RED);
                                break;
                            }
                        case "listgroups":
                            ChatManager.sendMessage(player, ">> Groups", ChatColor.GRAY);
                            ChatManager.sendMessage(player, schematicManager.listGroups(), ChatColor.BLUE);
                            break;
                        case "listslots":
                            if(args.length == 3) {
                                ChatManager.sendMessage(player, ">> Slots", ChatColor.GRAY);
                                ChatManager.sendMessage(player, schematicManager.listSlots(args[2]), ChatColor.BLUE);
                            }else {
                                ChatManager.sendHelp(player, HelpType.SCHEMATIC);
                            }
                            break;
                        case "teleport":
                            ChatManager.sendMessage(player, "Teleporting you to the Schematics...", ChatColor.BLUE);
                            player.teleport(new Location(worldManager.getWorld(), 0, 119, 520));
                        default:
                            ChatManager.sendHelp(player, HelpType.SCHEMATIC);
                            break;
                    }
                break;
            default:
                ChatManager.sendHelp(player, HelpType.GENERAL);
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