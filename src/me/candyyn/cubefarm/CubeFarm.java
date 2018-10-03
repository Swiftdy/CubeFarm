package me.candyyn.cubefarm;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.candyyn.cubefarm.commands.BalanceCommand;
import me.candyyn.cubefarm.commands.FarmCommand;
import me.candyyn.cubefarm.events.OnPlayerInteract;
import me.candyyn.cubefarm.events.OnPlayerJoin;
import me.candyyn.cubefarm.events.OnPlayerQuit;
import me.candyyn.cubefarm.events.build.OnBlockBreak;
import me.candyyn.cubefarm.events.build.OnBlockPlace;
import me.candyyn.cubefarm.events.enchant.OnInventoryClick;
import me.candyyn.cubefarm.events.enchant.OnPrepareItemEnchant;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.manager.BalanceManager;
import me.candyyn.cubefarm.manager.FarmManager;
import me.candyyn.cubefarm.manager.SchematicManager;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.manager.playerdata.PlayerData;
import me.candyyn.cubefarm.manager.playerdata.PlayerDataManager;
import me.candyyn.cubefarm.schematic.SchematicGrid;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class CubeFarm extends JavaPlugin implements Listener {

    private static WorldManager worldManager;
    private static FarmManager farmManager;
    private static FarmGrid farmGrid;
    private static SchematicGrid schematicGrid;
    private static SchematicManager schematicManager;
    private static PlayerDataManager playerDataManager;
    private static BalanceManager balanceManager;

    private static CubeFarm instance;

    public void onEnable() {

        // Warning: Singleton-Pattern, potentially unsafe
        // TODO: Consider alternatives
        instance = this;

        this.saveDefaultConfig();

        worldManager = new WorldManager(instance);
        worldManager.createFarmWorld();

        schematicGrid = new SchematicGrid(worldManager);
        playerDataManager = new PlayerDataManager(instance);
        farmGrid = new FarmGrid(worldManager, instance, schematicGrid);
        farmManager = new FarmManager(worldManager, instance, farmGrid);
        schematicManager = new SchematicManager(worldManager, instance, schematicGrid);
        balanceManager = new BalanceManager(this, playerDataManager, worldManager);

        this.getCommand("farm").setExecutor(new FarmCommand(instance, worldManager, farmGrid, schematicGrid, schematicManager, farmManager, playerDataManager));
        this.getCommand("balance").setExecutor(new BalanceCommand(instance, worldManager, playerDataManager, balanceManager));
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnPrepareItemEnchant(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);


        schematicManager.loadSchematics();
        ArrayList<PlayerData> playerData = playerDataManager.loadPlayersData();
        farmManager.loadFarms(playerData);
    }

    

    public static CubeFarm getInstance() {
        return instance;
    }
    
    public static FarmManager getFarmManager() {
        return farmManager;
    }

    public static FarmGrid getFarmGrid() {
        return farmGrid;
    }

    public static SchematicGrid getSchematicGrid() { return schematicGrid; }
    public static PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public static WorldManager getWorldManager() { return worldManager; }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public void onDisable() {
        farmManager.saveFarms();
        playerDataManager.savePlayersData();
        schematicManager.saveSchematics();
        this.saveDefaultConfig();
    }
}

