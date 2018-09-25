package me.candyyn.cubefarm.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.utils.Constants;
import me.candyyn.cubefarm.utils.IslandSection;
import me.candyyn.cubefarm.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class FarmManager {

    private final CubeFarm cubeFarm;
    private final FarmGrid farmGrid;
    private final Map<UUID, Farm> farmCache = new HashMap<>();

    public FarmManager(CubeFarm cubeFarm) {
        this.cubeFarm = cubeFarm;
        this.farmGrid = new FarmGrid(100, 100);
    }

    /**
     * This method returns true if player has a farm saved in either cache or memory. Checks cache
     * before memory.
     */
    public boolean hasFarm(UUID uniqueId) {
        return hasFarmInCache(uniqueId) || hasFarmInMemory(uniqueId);
    }

    /**
     * This method checks if player has a farm saved in memory (aka file storage.)
     */
    public boolean hasFarmInMemory(UUID uniqueId) {
        return cubeFarm.getConfig().isSet("farms." + uniqueId.toString());
    }

    /**
     * This method checks if player has a farm saved and loaded into local cache.
     */
    public boolean hasFarmInCache(UUID uniqueId) {
        return farmCache.containsKey(uniqueId);
    }

    public Farm createFarm(Player player, WorldManager worldManager) {
        IslandSection section = farmGrid.claimSpace(player.getUniqueId());
        Farm farm = new Farm(player.getUniqueId(), section, worldManager);
        saveFarm(farm);
        // Paste schematic
        return farm;
    }

    public Farm getFarm(UUID uniqueId, WorldManager worldManager) {
        if (!hasFarmInCache(uniqueId)) {
            if (!hasFarmInMemory(uniqueId)) {
                return null;
            }
            return loadFarm(uniqueId, worldManager);
        }
        return farmCache.get(uniqueId);
    }

    public Farm loadFarm(UUID uniqueId, WorldManager worldManager) {
        if (!hasFarmInMemory(uniqueId)) {
            return null;
        }

        ConfigurationSection section = cubeFarm.getConfig()
                .getConfigurationSection("farms." + uniqueId.toString());
        String[] stringSection = section.getString("island").split("::");
        IslandSection islandSection = new IslandSection(Integer.parseInt(stringSection[0]),
                Integer.parseInt(stringSection[1]));

        Farm farm = new Farm(uniqueId, islandSection, worldManager);
        farm.setUpgrade(section.getInt("upgrade"));
        // Loop through friends convert to uuid
        List<UUID> friends = new ArrayList<>();
        section.getStringList("friends").forEach(friend -> friends.add(UUID.fromString(friend)));
        farm.setFriends(friends);
        farm.setBanned(section.getBoolean("banned"));
        farm.setSpawnLocation(Utils.deserializeLocation(section.getString("spawn")));
        return farm;
    }

    public void saveFarm(Farm farm) {
        ConfigurationSection section = cubeFarm.getConfig()
                .createSection("farms." + farm.getIslandOwner().toString());
        section.set("island", farm.getSection().toString());
        section.set("upgrade", farm.getUpgrade());
        section.set("friends", farm.getFriends());
        section.set("banned", farm.isBanned());
        section.set("spawn", Utils.serializeLocation(farm.getSpawnLocation()));
        cubeFarm.saveConfig();
    }

    public FarmGrid getFarmGrid() {
        return farmGrid;
    }

    public Map<UUID, Farm> getFarmCache() {
        return farmCache;
    }
}
