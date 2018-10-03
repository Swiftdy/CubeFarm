package me.candyyn.cubefarm.schematic;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.utils.Constants;
import me.candyyn.cubefarm.utils.FarmRectangle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class FarmSchematic {
    private FarmRectangle rectangle;
    private WorldManager worldManager;



    public FarmRectangle getBounds() {
        return rectangle;
    }

    public FarmSchematic (int x, int y, WorldManager worldManager)
    {
        this.worldManager = worldManager;
        this.rectangle = new FarmRectangle(x, y, 8, 8);

    }
    
    public void paste(Location location, EditSession session) {
        Vector pos1 = new com.sk89q.worldedit.Vector(getBounds().x, Constants.FARM_Y_COORD, getBounds().y);
        Vector pos2 = new com.sk89q.worldedit.Vector(getBounds().getX2() - 1, Constants.FARM_Y_COORD + Constants.FARM_HEIGHT, getBounds().getY2() - 1);
        CuboidRegion copyRegion = new CuboidRegion(pos1, pos2);
        BlockArrayClipboard lazyCopy = session.lazyCopy(copyRegion);
        Schematic schematic = new Schematic(lazyCopy);
        boolean pasteAir = true;
        com.sk89q.worldedit.Vector to = new Vector(location.getX(), location.getY(), location.getZ());
        schematic.paste(session, to, pasteAir);
        session.flushQueue();
    }

    // Warning! Dangerous. Clears region and replaces with air.
    public void clear() {
        EditSession session = worldManager.getEditSession();
        Region region = new CuboidRegion(new Vector(getBounds().x, Constants.FARM_HEIGHT, getBounds().y),
                new Vector(getBounds().getX2(),  Constants.FARM_Y_COORD + Constants.FARM_HEIGHT, getBounds().getY2())); // Get region of full area
        session.setBlocks(region, BlockTypes.AIR);
    }

    public void generateSpot() {
        Bukkit.getLogger().warning("x2: " + getBounds().getX2() + "  y2: " + getBounds().getY2());
        World world = worldManager.getWorld();
        EditSession session = worldManager.getEditSession(); //104 = y
        for (int x = getBounds().x; x < getBounds().getX2(); x++) {
            for (int z = getBounds().y; z < getBounds().getY2(); z++) {
                for (int y = Constants.FARM_Y_COORD; y < Constants.FARM_Y_COORD + Constants.FARM_HEIGHT; y++) {
                    int realHeight = y - Constants.FARM_Y_COORD;

                    BlockType currentType = BlockTypes.AIR;
                    if(realHeight == 0) {
                        currentType = BlockTypes.BEDROCK;
                    }
                    if(realHeight >= 1 && realHeight < 8 ) {
                        currentType = BlockTypes.DIRT;
                        
                    }
                    if(realHeight == 8) {
                        currentType = BlockTypes.GRASS_BLOCK;
                    }
                    //Bukkit.getLogger().info("X: " + x + " Y: " + y + " Z: " + z + " setting to " + currentType.getName());
                    //world.getBlockAt(x, y, z).setType(currentType);

                    //session.setBlock(new Vector(x, y, z), new BaseBlock(currentType.getId(), currentType.getData().getModifiers()));
                    session.setBlock(x, y, z, currentType);
                    session.flushQueue();

                    //session.setBlock(x, y, z, currentType);
                    //session.setBlock(x, y, z, new BaseBlock(currentType.getLegacyId(), 0));
                }
            }
        }
    }
}
