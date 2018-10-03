package me.candyyn.cubefarm.farm;

import com.sk89q.worldedit.EditSession;
import me.candyyn.cubefarm.manager.SchematicManager;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.schematic.FarmSchematic;
import me.candyyn.cubefarm.schematic.SchematicGrid;
import me.candyyn.cubefarm.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.awt.*;

public class FarmSection {


    public String schematicGroupName;
    public int schematicSlot;
    private int x;
    private int y;
    private SchematicGrid schematicGrid;
    private WorldManager worldManager;
    private Farm parent;

    public FarmSection(Farm parent, int x, int y, String schematicGroupName, int schematicSlot, SchematicGrid schematicGrid, WorldManager worldManager) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.schematicGroupName = schematicGroupName;
        this.schematicSlot = schematicSlot;
        this.schematicGrid = schematicGrid;
        this.worldManager = worldManager;
    }

    public int getSchematicSlot() {
        return schematicSlot;
    }

    public String getSchematicGroupName() {
        return schematicGroupName;
    }

    public SchematicGrid getSchematicGrid() {
        return schematicGrid;
    }


    public int getX() {
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    // Get local chunked X
    public int getLCX() {
        return (getX() - parent.getBounds().x) / Constants.CHUNK_COUNT;
    }
    // Get local chunked Y
    public int getLCY() {
        return (getY() - parent.getBounds().y) / Constants.CHUNK_COUNT;
    }


    public void generate(EditSession session) {
        FarmSchematic schematic = schematicGrid.getSlot(schematicGroupName, schematicSlot);
        schematic.paste(new Location(worldManager.getWorld(), x, Constants.FARM_HEIGHT, y), session);
    }
}
