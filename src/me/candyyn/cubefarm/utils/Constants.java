package me.candyyn.cubefarm.utils;


import org.bukkit.inventory.MerchantRecipe;

public class Constants {

    public static final int FARM_Y_COORD = 100;
    public static final int FARM_CHUNK_SIZE = 15;
    public static final int CHUNK_COUNT = 8;
    public static final int FARM_SIZE = FARM_CHUNK_SIZE * CHUNK_COUNT;
    public static final int VOID_GAP = 100;
    //public static final int FARM_CENTRE = ((FARM_SIZE / 8) / 2) + 1;
    public static final int FARM_CENTRE = ((FARM_SIZE / CHUNK_COUNT) / 2) ;

    public static final int GRID_Z = 500;


    public static final int FARM_HEIGHT = 100; //TODO: set farm height
}
