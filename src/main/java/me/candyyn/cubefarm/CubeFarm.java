package me.candyyn.cubefarm;

import me.candyyn.cubefarm.commands.FarmCommand;
import me.candyyn.cubefarm.manager.FarmManager;
import me.candyyn.cubefarm.manager.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CubeFarm extends JavaPlugin {

    private WorldManager worldManager;
    private FarmManager farmManager;
    public void onEnable() {
        worldManager = new WorldManager(this);
        worldManager.createFarmWorld();
        farmManager = new FarmManager(this);
        this.getCommand("farm").setExecutor(new FarmCommand(this));
    } // Nope idk why you had it

    public void onDisable() {
        this.saveDefaultConfig();
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public FarmManager getFarmManager() {
        return farmManager;
    }
}
