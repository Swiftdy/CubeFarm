package me.candyyn.cubefarm.utils;

public class IslandSection {

    private final int row, column;

    public IslandSection(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.valueOf(row + "::" + column);
    }
}
