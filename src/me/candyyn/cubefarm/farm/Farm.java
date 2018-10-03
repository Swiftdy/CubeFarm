package me.candyyn.cubefarm.farm;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.manager.ChatManager;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.manager.playerdata.PlayerData;
import me.candyyn.cubefarm.schematic.SchematicGrid;
import me.candyyn.cubefarm.utils.ChunkAccessibilityType;
import me.candyyn.cubefarm.utils.Constants;
import me.candyyn.cubefarm.utils.FarmRectangle;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Farm {
    private final PlayerData playerData;
    //private final IslandSection section;
    private int upgrade;
    
    private Location spawnLocation;
    private Location centreLocation;
    
    private List<UUID> friends;
    private boolean banned;
    private int index;
    private WorldManager worldManager;
    private SchematicGrid schematicGrid;
    private CubeFarm cubeFarm;
    private Random randomGenerator;
    private boolean isBuilding;

    private FarmSection[][] farmSections = new FarmSection[Constants.FARM_SIZE / 8][Constants.FARM_SIZE / 8];

    public Farm(PlayerData playerData, WorldManager worldManager, CubeFarm cubefarm, SchematicGrid schematicGrid) {

        this.playerData = playerData;
        this.index = playerData.getFarmIndex(); // always farms.size()
        this.worldManager = worldManager;
        this.schematicGrid = schematicGrid;
        this.upgrade = 0;
        this.banned = false;
        this.friends = new ArrayList<>();
        this.randomGenerator = new Random();
        this.cubeFarm = cubefarm;
        this.isBuilding = false;

        FarmRectangle rectangle = getBounds();
        Point p = rectangle.getCenter();
        this.spawnLocation = new Location(worldManager.getWorld(), rectangle.x, Constants.FARM_Y_COORD, rectangle.y);
        this.centreLocation = new Location(worldManager.getWorld(), rectangle.x + Constants.FARM_SIZE / 2f, Constants.FARM_Y_COORD + 8 + 1, rectangle.y + Constants.FARM_SIZE / 2f);
        initialiseSections();
    }

    private void initialiseSections() {
        for(int x = 0; x < Constants.FARM_SIZE / 8; x++) {
            for(int y = 0; y < Constants.FARM_SIZE / 8; y++) {
                int ax = spawnLocation.getBlockX() + (x * 8); // <<<
                int ay = spawnLocation.getBlockZ() + (y * 8);

                int max = schematicGrid.getGroup("trees").size();
                int index = randomGenerator.nextInt(max);

                // Set Trees
                farmSections[x][y] = new FarmSection(this, ax, ay, "trees", index, schematicGrid, worldManager);

                // Set Centre
                if(x == Constants.FARM_CENTRE && y == Constants.FARM_CENTRE) {
                    farmSections[Constants.FARM_CENTRE][Constants.FARM_CENTRE] =
                            new FarmSection(this, ax, ay, "grass", 0, schematicGrid, worldManager);
                }

            }
        }
    }

    public boolean isBuildableSection(int x, int y) {
        FarmSection section = getFarmSection(x, y);
        if(section == null) { return false; }
        return !getFarmSection(x, y).schematicGroupName.equals("trees");
    }

    public FarmSection[][] getFarmSections() {
        return farmSections;
    }

    public Region getRegion() {
        return new CuboidRegion(new Vector(getBounds().x, Constants.FARM_HEIGHT, getBounds().y),
                new Vector(getBounds().getX2(), Constants.FARM_Y_COORD + Constants.FARM_HEIGHT, getBounds().getY2())); // Get region of full area
    }

    private List<BlockVector2D> convertBlockVector(Set<Vector2D> vectors) {
        List<BlockVector2D> output = new ArrayList<>();
        for(Vector2D vector : vectors) {
            output.add(new BlockVector2D(vector.x, vector.z));
        }
        return output;
    }
    
    public String[] getData() {
        String[] data = new String[farmSections.length*farmSections.length];
        int index = 0;
        for(int x = 0; x < Constants.FARM_CHUNK_SIZE; x++) {
            for (int y = 0; y < Constants.FARM_CHUNK_SIZE; y++) {
                String stringData =  farmSections[x][y].schematicGroupName + "," + farmSections[x][y].schematicSlot;
                Bukkit.getLogger().info("X: " + x + " Y: " + y + " Data: " + stringData);

                if(farmSections[x][y].schematicGroupName.equals("grass")) {
                    Bukkit.getLogger().info("Grass: " +  x + ", " + y + " => " + stringData);
                }

                data[index] = stringData;
                index++;
            }
        }
        return data;
    }

    public void setData(String[] data) {
        int index = 0;
        int x = 0;
        int y = 0;
        for(String string : data) {
            String[] args = string.split(",");
            String groupName = args[0];
            int slotIndex = Integer.parseInt(args[1]);

            if(x >= Constants.FARM_CHUNK_SIZE - 1) {
                y++;
            }
            x = index - (y * Constants.FARM_CHUNK_SIZE); //             x = 16

            farmSections[x][y].schematicSlot = slotIndex;
            farmSections[x][y].schematicGroupName = groupName;
            index++;
        }
    }

    public void buildSections() {
        setBuilding(true);
        ConfigurationSection config = cubeFarm.getConfig();
        config.set("farmamount", config.getInt("farmamount") + 1);

        final int farmArea = Constants.FARM_CHUNK_SIZE * Constants.FARM_CHUNK_SIZE;
        final Player player = Bukkit.getPlayer(playerData.getUUID());
        final EditSession session = worldManager.getEditSession();
        Region region = getRegion(); // Get region of full area
        TaskManager.IMP.async(() -> {
            int counter = 0;
            for(int x = 0; x < Constants.FARM_SIZE / 8; x++) {
                for(int y = 0; y < Constants.FARM_SIZE / 8; y++) {
                    FarmSection section = farmSections[x][y];
                    section.generate(session);
                    counter++;
                    if(counter % (int)Math.round(farmArea / 10.0) == 0) {
                        int fixed_percent = (int)Math.round(( (double)counter / (double) farmArea ) * 100 );
                        ChatManager.sendMessage(player, "Generating farm: " + fixed_percent + "%", ChatColor.BLUE);
                    }

                }
            }
            ChatManager.sendMessage(player, "Generating farm: 100%", ChatColor.BLUE);
            ChatManager.sendMessage(player, "Farm Created. Teleport to your farm by doing /farm home", ChatColor.AQUA);
            session.flushQueue(); // Flush the queue
            setBuilding(false); // No longer building
        });
    }

    //deletes Async players Farm
    public void clear() {
        if(!isBuilding) {
            TaskManager.IMP.async(() -> {
                EditSession session = worldManager.getEditSession();
                session.setFastMode(false);
                Region region = getRegion();
                region.polygonize(10);
                session.setBlocks(region, BlockTypes.AIR);
                //session.fixLighting();
                session.flushQueue();
            });
        }
    }

    public FarmSection getFarmSection(int x, int y) {

        Bukkit.getLogger().info(" getFarnSection X: " + x + " Y: " +y);

        if(x < 0 || y < 0 || x > Constants.FARM_CHUNK_SIZE - 1 || y > Constants.FARM_CHUNK_SIZE - 1) {
            return null;
        }
        return getFarmSections()[x][y];
    }

    // Is one of the sides a grass chunk
    public ChunkAccessibilityType isChunkAccessible(int x, int y) {
        FarmSection[] sections = new FarmSection[4];
        sections[0] = getFarmSection(x - 1, y);
        sections[1] = getFarmSection(x, y - 1);
        sections[2] = getFarmSection(x + 1, y);
        sections[3] = getFarmSection(x, y + 1);

        ChunkAccessibilityType check = ChunkAccessibilityType.UNREACHABLE;
        for(int i = 0; i < sections.length; i++) {
            FarmSection section = sections[i];
            if(section == null) {
                return ChunkAccessibilityType.EMPTY;
            }
            if(!section.schematicGroupName.equals("trees")) {
                check = ChunkAccessibilityType.ACCESSIBLE;
            }
        }
        return check;
    }

    public boolean isChunkNull(int x, int y) {
        return getFarmSection(x, y) == null;
    }

    //Return playerData
    public PlayerData getPlayerData() {
        return playerData;
    }

    public FarmRectangle getBounds() {
        return new FarmRectangle(index * (Constants.FARM_SIZE + Constants.VOID_GAP), 0, Constants.FARM_SIZE, Constants.FARM_SIZE);
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

    public Location getCentreLocation() {
        return centreLocation;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public int getIndex() { return this.index;}

    public boolean isBanned() {
        return banned;
    }

    public void setBuilding(boolean building) {
        isBuilding = building;
    }

    public boolean getBuilding() {
        return isBuilding;
    }
    
    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void teleport(Player player) {
        player.teleport(centreLocation, PlayerTeleportEvent.TeleportCause.COMMAND);
    }

    public void setFriends(List<UUID> friends) {
        this.friends = friends;
    }
}
