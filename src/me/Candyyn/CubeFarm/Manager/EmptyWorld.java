package me.Candyyn.CubeFarm.Manager;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

public class EmptyWorld extends ChunkGenerator{

    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid){
        byte[][] result = new byte[world.getMaxHeight() / 16][];

        return result;
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 50, 50 + 1, 50);
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        List<Biome> whitelist = Arrays.asList(Biome.PLAINS);

        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                if (!whitelist.contains(biome.getBiome(X, Z))) {
                    biome.setBiome(X, Z, Biome.PLAINS);
                }
            }
        }

        return createChunkData(world);
    }


    @Deprecated
    public short[][] generateExtBlockSections(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes) {
        return this.generateExtBlockSections(world, random, x, z, biomes);
    }

    public byte[] generate(World world, Random rand, int chunkx, int chunkz) {
        return new byte[32768];
    }
    private void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }
}
