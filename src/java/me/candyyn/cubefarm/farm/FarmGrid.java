package java.me.candyyn.cubefarm.farm;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.me.candyyn.cubefarm.utils.IslandSection;

public class FarmGrid {

    private UUID[][] islandGrid;

    public FarmGrid(int row, int col) {
        this.islandGrid = new UUID[row][col];
    }


    // You'll need to create a method to load all islands on startup.
    // Ye, that was kinda what was confusing me
    // How're you saving it? Flat file



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
        return row < islandGrid.length && col < islandGrid[row].length && (
                islandGrid[row][col] == null
                        || islandGrid[row][col] == null);
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
