package me.candyyn.cubefarm.farm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.utils.Constants;
import me.candyyn.cubefarm.utils.IslandSection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Farm {
    private final UUID islandOwner;
    private final IslandSection section;
    private int upgrade;
    private Location spawnLocation;
    private List<UUID> friends;
    private boolean banned;

    public Farm(UUID islandOwner, IslandSection section, WorldManager worldManager) {
        this.islandOwner = islandOwner;
        this.section = section;
        this.upgrade = 0;
        this.banned = false;
        this.friends = new ArrayList<>();
        int x = section.getRow() == 0 ? section.getRow() : section.getRow() * 520;
        int z = section.getColumn() == 0 ? section.getColumn() : section.getColumn() * 520;
        this.spawnLocation = new Location(worldManager.getWorld(), x, Constants.FARM_Y_COORD, z);
    }

    public UUID getIslandOwner() {
        return islandOwner;
    }

    public IslandSection getSection() {
        return section;
    }

    public int getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = upgrade;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void teleport(Player player) {
        player.teleportAsync(spawnLocation);
    }

    public void setFriends(List<UUID> friends) {
        this.friends = friends;
    }
}
