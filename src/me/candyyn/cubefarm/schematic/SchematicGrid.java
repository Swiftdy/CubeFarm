package me.candyyn.cubefarm.schematic;

import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.utils.Constants;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SchematicGrid {

    private WorldManager worldManager;
           //Map<String, Map<Integer, ArrayList<FarmSchematic>>>
    private Map<String, ArrayList<FarmSchematic>> schematics;

    public SchematicGrid(WorldManager worldManager) {
        schematics = new LinkedHashMap<>();
        this.worldManager = worldManager;
    }

    public Map<String, ArrayList<FarmSchematic>> getSchematics() {
        return schematics;
    }

    public boolean deleteGroup(String group) {
        if(checkGroup(group)) {
            schematics.remove(group);
            return true;
        }
        return false;
    }

    public boolean deleteSlot(String group, int index) {
        if (checkGroup(group)) {
            ArrayList<FarmSchematic> groupArray = getGroup(group);
            groupArray.set(index, null); // Set index to null
            return true;
        }
        return false;
    }

    public boolean checkGroup(String group) {
        return schematics.containsKey(group);
    }

    public ArrayList<FarmSchematic> createGroup(String group) {
        ArrayList<FarmSchematic> newGroup = new ArrayList<FarmSchematic>();
        schematics.put(group,newGroup);
        return newGroup;
    }

    public ArrayList<FarmSchematic> getGroup(String group) {
        return schematics.get(group);
    }

    public void clearGroup(String group) {
        ArrayList<FarmSchematic> groupArray = getGroup(group);
        for(FarmSchematic schematic : groupArray) {
            schematic.clear();
        }
    }

    private FarmSchematic compileSchematic(int groupIndex, int index) {
        return new FarmSchematic((groupIndex * 9), Constants.GRID_Z + (index * 9), worldManager);
    }

    public void insertSchematic(String group) {
        insertSchematic(group, false);
    }
    public void insertSchematic(String group, boolean isNull) {
        ArrayList<FarmSchematic> groupArray = getGroup(group);
        FarmSchematic newSchematic;
        if(isNull) {
            newSchematic = null;
        } else {
            newSchematic = compileSchematic(schematics.size(), groupArray.size());
        }
        groupArray.add(newSchematic);
        //Bukkit.getLogger().warning(newSchematic.toString());
    }

    public int getGroupIndex(String group) {
        int index = 0;
        for(String searchGroup : schematics.keySet()) {
            index++;
            if(group.equals(searchGroup)) {
                return index;
            }
        }
        return -1;
    }

    public FarmSchematic createSchematic(String group) {
        if(!checkGroup(group)) {
            return null;
        }
        ArrayList<FarmSchematic> groupArray = getGroup(group);

        //   <-------> 0 1 2 3
        //             0 9 18 27
        //

        int index = -1;
        int tempIndex = -1;
        boolean replace = false;
        for(FarmSchematic schematic : groupArray) {
            tempIndex++;
            if(schematic == null) {
                index = tempIndex;
                replace = true;
                break;
            }
        }
        if(index == -1) {
            index = groupArray.size(); // -1
        }

        FarmSchematic newSchematic = compileSchematic(getGroupIndex(group), index);

        if(replace) {
            groupArray.set(index, newSchematic);
        } else {
            groupArray.add(newSchematic);
        }

        newSchematic.generateSpot();

        //Bukkit.getLogger().warning("X: " + newSchematic.getBounds().x + " Y:" + Constants.FARM_Y_COORD + " Z:" + newSchematic.getBounds().y);

        return newSchematic;
    }

    public FarmSchematic getSlot(String group, int index) {
        return getGroup(group).get(index);
    }
}
