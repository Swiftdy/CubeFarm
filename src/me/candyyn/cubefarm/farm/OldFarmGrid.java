package me.candyyn.cubefarm.farm;

import me.candyyn.cubefarm.utils.IslandSection;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class OldFarmGrid {

    private UUID[][] islandGrid;

    public OldFarmGrid(int row, int col) {
        this.islandGrid = new UUID[row][col];
    }

    public IslandSection getFreeSpace() {
        for (int row = 0; row < islandGrid.length; row++) {
            for (int col = 0; col < islandGrid[row].length; col++) {
                if (this.isFree(row, col)) {
                    return new IslandSection(row, col);
                }
            }
        }
        return null;
    }


    public boolean isFree(int row, int col) {

        boolean result = row < islandGrid.length && col < islandGrid[row].length && (
                islandGrid[row][col] == null
                        || islandGrid[row][col] == null);
        //here
        Bukkit.getLogger().info("" + result);
        return result;
    }

    public void claimSpace(UUID uniqueId, IslandSection section) {
        this.islandGrid[section.getRow()][section.getRow()] = uniqueId;
    }


    public IslandSection claimSpace(UUID uniqueId) {
        IslandSection freeSpace = this.getFreeSpace();
        this.claimSpace(uniqueId, freeSpace);
        return freeSpace;
    }


    public void unclaimSpace(int row, int col) {
        if (isFree(row, col)) {
            return;
        }
        this.islandGrid[row][col] = null;
    }

    public Optional<UUID> getIsland(int row, int col) {
        return this.isFree(row, col) ? Optional.empty() : Optional.of(this.islandGrid[row][col]);
    }

    public UUID[][] getIslandGrid() {
        return Arrays.copyOf(this.islandGrid, this.islandGrid.length);
    }
}
