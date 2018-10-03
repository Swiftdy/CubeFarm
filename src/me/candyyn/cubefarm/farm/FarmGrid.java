package me.candyyn.cubefarm.farm;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.manager.playerdata.PlayerData;
import me.candyyn.cubefarm.schematic.SchematicGrid;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FarmGrid {

        private WorldManager worldManager;
        private ArrayList<Farm> farms;
        private Map<UUID, Farm> farmCache;
        private SchematicGrid schematicGrid;
        private CubeFarm cubeFarm;

        public FarmGrid(WorldManager worldManager, CubeFarm cubeFarm, SchematicGrid schematicGrid) {
            farms = new ArrayList<>();
            farmCache = new HashMap<>();
            this.cubeFarm = cubeFarm;
            this.worldManager = worldManager;
            this.schematicGrid = schematicGrid;
        }

        public ArrayList<Farm> getFarms() {
            return farms;
        }

        private Farm getFarmCache(UUID owner) {
            return farmCache.get(owner);
        }

        private void cacheFarm(UUID owner, Farm farm) {
            farmCache.put(owner, farm);
        }

        public Farm getFarm(UUID owner) {
            Farm cachedFarm = getFarmCache(owner);
            if(cachedFarm == null) {
                for (Farm farm : farms) {
                    if (farm.getPlayerData().getUUID() == owner) {
                        cacheFarm(owner, farm);
                        return farm;
                    }
                }
            } else {
                return cachedFarm;
            }
            return null;
        }

        public boolean hasFarm(UUID owner) {
            return getFarm(owner) != null;
        }

        public Farm getFarm(int index) {
            return farms.get(index);
        }

        public Farm getFarm(Location location) {
            for (Farm farm : farms) {
                if(farm.getBounds().contains(new Point(location.getBlockX(), location.getBlockZ()))) {
                    return farm;
                }
            }
            return null;

        }

        public Farm getFarmByIndex(int index) {
            for(Farm farm : farms) {
                if(farm.getIndex() == index) {
                    return farm;
                }
            }
            return null;
        }

        public FarmSection getSection(Farm farm, Location location) {
            /*
            
            dy:   y=0   0 - 100     -100    math.abs(-100) = 100
            dy:   getBlockZ()

            dx:   100 - 108
            dx:   -8 = 8 / 8 = 1
            dx:   200 - 208
            dx:   -8 = 8 / 8 = 1
             */

            Bukkit.getLogger().info("Farm Index: " + farm.getIndex() + " Location: " + location.getX() + ", " + location.getZ());
            Bukkit.getLogger().info("Farm Bounds: [" + farm.getBounds().x + ", " + farm.getBounds().y + "] - [" + farm.getBounds().getX2() + ", " + farm.getBounds().getY2() + "] ");
            double dx = Math.abs(Math.round((farm.getBounds().x - location.getBlockX()))); // 280   account for dx
            double dy =  Math.abs(Math.round(farm.getBounds().y - location.getBlockZ())); // 56
            int bdx = (int)(dx / 8);
            int bdy = (int)(dy / 8);

            Bukkit.getLogger().info("dx: " + dx + " dy: " + dy + " bdx: " + bdx + " bdy: " + bdy);

            return farm.getFarmSection(bdx, bdy);
        }

        public FarmSection getSection(Farm farm, int x, int y) {

            if(x < 0  || y < 0) {
                return null;
            }
            return farm.getFarmSection(x, y);
        }



        public void deleteFarm(UUID uuid) {
            farms.remove(getFarm(uuid));
            farmCache.remove(uuid);
        }

        // Directly adds farm to cache
        public void addFarm(Farm farm) {
            farms.add(farm);
        }

        // Adds farm to cache and returns instance
        public Farm createFarm(PlayerData playerData) {
            Farm farm = new Farm(playerData, worldManager, cubeFarm, schematicGrid);
            addFarm(farm);
            cacheFarm(playerData.getUUID(), farm);
            return farm;
        }

        // Generates new farm
        public Farm generateFarm(PlayerData owner) {
            Farm farm = createFarm(owner);
            return farm;
        }
}


