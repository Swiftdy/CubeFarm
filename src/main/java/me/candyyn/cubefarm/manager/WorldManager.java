package me.candyyn.cubefarm.manager;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.world.EmptyWorld;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class WorldManager {
    private CubeFarm cubefarm;

    public WorldManager(CubeFarm cubefarm) {
        this.cubefarm = cubefarm;
    }

    public void createFarmWorld() {
        String worldName = cubefarm.getConfig().getString("world");
        if (Bukkit.getWorld(worldName) == null) {
            WorldCreator creator = new WorldCreator(worldName);
            creator.generator(new EmptyWorld());
            creator.createWorld();
        }
    }
}
