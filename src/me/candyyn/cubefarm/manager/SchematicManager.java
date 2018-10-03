package me.candyyn.cubefarm.manager;

import com.boydti.fawe.object.schematic.Schematic;
import com.mysql.fabric.xmlrpc.base.Array;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.schematic.FarmSchematic;
import me.candyyn.cubefarm.schematic.SchematicGrid;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.rowset.spi.SyncFactory;
import java.util.*;

public class SchematicManager {

    private CubeFarm cubeFarm;
    private SchematicGrid schematicGrid;
    private WorldManager worldManager;
    public SchematicManager(WorldManager worldManager, CubeFarm cubeFarm, SchematicGrid schematicGrid) {
        this.cubeFarm = cubeFarm;
        this.schematicGrid = schematicGrid;
        this.worldManager = worldManager;
    }

    //Loading Schematics
    public void loadSchematics() {

        FileConfiguration config = cubeFarm.getConfig();
        Bukkit.getLogger().info("[CubeFarm] Loading Schematics");
        ConfigurationSection sec = config.getConfigurationSection("schematics");
        int loadedsche = 0;

        //Checks if none was found
        if(sec == null) {
            Bukkit.getLogger().warning("[CubeFarm] No Schematics was found!");
            return;
        }
        for(String name : sec.getKeys(false)){
            schematicGrid.createGroup(name);
            List<String> stringList = config.getStringList("schematics." + name);

            for (int i=0; i<stringList.size(); i++) {
                if(stringList.get(i).equals("null")) {
                    schematicGrid.insertSchematic(name, true);
                }else {
                    schematicGrid.insertSchematic(name);
                }
                loadedsche++;
            }
        }
        Bukkit.getLogger().info("[CubeFarm] Loaded "+ loadedsche+" Schematics");
    }
    //Save all the Schematics
    public void saveSchematics() {
        FileConfiguration config = cubeFarm.getConfig();
        int x = 0;
        for(String group : schematicGrid.getSchematics().keySet()) {
            x++;
            ArrayList<FarmSchematic> groupArray = schematicGrid.getGroup(group);
            for(FarmSchematic slot : groupArray) {
                ArrayList<String> myStringList = new ArrayList<>();
                for (int i = 0; i < groupArray.size(); i++) {
                    if(groupArray.get(i) == null) {
                        myStringList.add("null");
                    } else {
                        myStringList.add("" + i);
                    }
                }
                config.set("schematics." + group, myStringList);
            }
        }
        if(x == 0) {
            config.set("schematics", null);
        }
        cubeFarm.saveConfig();
    }

    //List Schematic groups
    public String listGroups() {
        Map<String, ArrayList<FarmSchematic>> schematics = schematicGrid.getSchematics();
        StringBuilder constructedList = new StringBuilder();
        for(String group : schematics.keySet()) {
            constructedList.append(group).append(", ");
        }
        String finished = constructedList.substring(0, constructedList.length() -2) + ".";
        return finished;
    }
    //List Schematic slots for a group
    public String listSlots(String group) {
        ArrayList<FarmSchematic> groupArray = schematicGrid.getGroup(group);
        StringBuilder constructedList = new StringBuilder();
        int index = 0;
        for(FarmSchematic slot : groupArray) {
            String message;
            if(groupArray.get(index) == null) {
                message = "null";
            } else {
                message = "" + index;
            }
            constructedList.append(message).append(", ");
            index++;
        }
        String finished = constructedList.substring(0, constructedList.length() -2) + ".";
        return finished;
    }
}
