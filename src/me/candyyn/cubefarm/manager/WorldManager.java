package me.candyyn.cubefarm.manager;

import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.world.EmptyWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldManager {
    private CubeFarm cubefarm;

    public WorldManager(CubeFarm cubefarm) {
        this.cubefarm = cubefarm;
    }

    //Returns the farm world
    public World getWorld() {
        return Bukkit.getWorld(cubefarm.getConfig().getString("world"));
    }

    // Return a Edit session
    public EditSession getEditSession() {
        return new EditSessionBuilder(getWorld().getName()).autoQueue(true).fastmode(true).build();
    }

    //Either loads or creates a world
    public void createFarmWorld() {
        String worldName = cubefarm.getConfig().getString("world");
        if (Bukkit.getWorld(worldName) == null) {
            WorldCreator creator = new WorldCreator(worldName);
            creator.generator(new EmptyWorld());
            creator.createWorld();
        }

    }
}
