package me.Candyyn.CubeFarm.Manager;

import me.Candyyn.CubeFarm.Main;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.util.logging.Logger;

public class WorldManager { // Smh capital W

    private Logger log = Bukkit.getLogger();
    private final FarmGrid farmgrid;

    public WorldManager() {
        farmgrid = new FarmGrid(100, 100);
    }

    public void createFarmWorld() {
        String worldName = Main.getMain().getConfig().getString("world");
        if (Bukkit.getWorld(worldName) == null) {
            WorldCreator creator = new WorldCreator(worldName);
            creator.generator(new EmptyWorld());
            creator.createWorld();
        }

    }

    public FarmGrid getFarmgrid() {
        return farmgrid;
    }
}
