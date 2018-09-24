package me.Candyyn.CubeFarm;

import me.Candyyn.CubeFarm.Commands.Farm;
import me.Candyyn.CubeFarm.Manager.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main main; // Don't expose variables, defeats the purpose of OOP
    private WorldManager worldManager;

    public static Main getMain() {
        return main;
    }

    public void onEnable(){
        main = this;
        worldManager = new WorldManager();
        worldManager.createFarmWorld();

        this.getCommand("farm").setExecutor(new Farm(worldManager));
    } // Nope idk why you had it
    public void onDisable(){
        this.saveDefaultConfig();
    }


}