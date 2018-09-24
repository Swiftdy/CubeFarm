package me.Candyyn.CubeFarm.Manager;

import java.util.Arrays;
import java.util.Optional;

public class FarmGrid {
    private String[][] islandGrid;

    public FarmGrid(int row, int col) {
        this.islandGrid = new String[row][col];
    }


    // You'll need to create a method to load all islands on startup.
    // Ye, that was kinda what was confusing me
    // How're you saving it? Flat file



    public int[] getFreeSpace() {
        int[] freeSpace = new int[2];
        for (int row = 0; row < islandGrid.length; row++) {
            for (int col = 0; col < islandGrid[row].length; col++) {
                if (this.isFree(row, col)) {
                    freeSpace[0] = row;
                    freeSpace[1] = col;
                    return freeSpace;
                }
            }
        }
        return freeSpace;
    }


    public boolean isFree(int row, int col) {
        return row < islandGrid.length && col < islandGrid[row].length && (
                islandGrid[row][col] == null
                        || islandGrid[row][col].isEmpty());
    }

    public void claimSpace(String islandId, int row, int col) {
        this.islandGrid[row][col] = islandId;
    }


    public void claimSpace(String islandId) {
        int[] freeSpace = this.getFreeSpace();
        this.claimSpace(islandId, freeSpace[0], freeSpace[1]);
    }


    public void unclaimSpace(int row, int col) {
        if (isFree(row, col)) {
            return;
        }
        this.islandGrid[row][col] = null;
    }

    public Optional<String> getIsland(int row, int col) {
        return this.isFree(row, col) ? Optional.empty() : Optional.of(this.islandGrid[row][col]);
    }

    public String[][] getIslandGrid() {
        return Arrays.copyOf(this.islandGrid, this.islandGrid.length);
    }
}
