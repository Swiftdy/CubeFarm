package me.candyyn.cubefarm.manager;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.manager.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FarmManager {

    private WorldManager worldManager;
    private CubeFarm cubefarm;
    private FarmGrid farmGrid;

    public FarmManager (WorldManager worldManager, CubeFarm cubeFarm, FarmGrid farmGrid) {
        this.worldManager = worldManager;
        this.cubefarm = cubeFarm;
        this.farmGrid = farmGrid;
    }
    
    public Farm loadFarm(PlayerData playerData) {

        ConfigurationSection section = YamlConfiguration.loadConfiguration(getFarmFile(playerData.getFarmIndex()));
        //ConfigurationSection section = cubefarm.getConfig().getConfigurationSection("farms." + playerData.getFarmIndex());

        String uuid = section.getString("owner");

        if(!playerData.getUUID().toString().equals(uuid)) {
            Bukkit.getLogger().info(playerData.getUUID().toString() + " =/= " + uuid);
            Bukkit.getLogger().warning("The farm owner and player data UUID didn't match!");
            return null;
        }

        Farm farm = farmGrid.createFarm(playerData);
        farm.setUpgrade(section.getInt("upgrade"));

        List<String> mapStringList = section.getStringList("map");
        String[] mapStringArray = new String[mapStringList.size()];
        mapStringArray = mapStringList.toArray(mapStringArray);
        farm.setData(mapStringArray);
        // Loop through friends convert to uuid
        //List<UUID> friends = new ArrayList<>();
        //section.getStringList("friends").forEach(friend -> friends.add(UUID.fromString(friend)));

        return farm;
    }

    public File loadFarmConfig(PlayerData playerData) {
        if(hasFarmConfig(playerData)) {
            File file = getFarmFile(playerData.getFarmIndex());
            return file;
        }else {
            File file = new File(cubefarm.getDataFolder() + "/farms/", playerData.getFarmIndex() + ".yml");
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bukkit.getLogger().info(cubefarm.getDataFolder() + "/farms/" + playerData.getFarmIndex() + ".yml");
            return file;
        }
    }

    public boolean hasFarmConfig(PlayerData playerData) {
        File file = getFarmFile(playerData.getFarmIndex());
        return file.exists();
    }

    public boolean doesFarmExistConfig(Farm farm) {
        File file = getFarmFile(farm.getPlayerData().getFarmIndex());
        return file.exists();
    }

    public void saveFarmConfig(Farm farm) {
        File file = loadFarmConfig(farm.getPlayerData());
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        //ConfigurationSection section = configuration;
        configuration.set("owner", farm.getPlayerData().getUUID().toString());
        configuration.set("upgrade", farm.getUpgrade());

        String[] map = farm.getData();
        configuration.set("map", map);
        //section.set("spawn", Utils.serializeLocation(farm.getSpawnLocation()));
        try {
            configuration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cubefarm.saveConfig();
    }

    public File getFarmFile(int index) {
        return new File(cubefarm.getDataFolder() + "/farms/" + index + ".yml");

    }

    public void createFarmFile(Farm farm) {

    }

    public void saveFarm(Farm farm) {

        // Farm Index

        saveFarmConfig(farm);

     /*   ConfigurationSection section = cubefarm.getConfig().createSection("farms." + farm.getIndex());
        section.set("owner", farm.getPlayerData().getUUID().toString());
        section.set("upgrade", farm.getUpgrade());

        //section.set("spawn", Utils.serializeLocation(farm.getSpawnLocation()));
        cubefarm.saveConfig();*/
    }
    
    public boolean hasFarm(PlayerData playerData) {
        Bukkit.getLogger().warning("Farm index: " + playerData.getFarmIndex() + " != -1");
        return playerData.getFarmIndex() != -1;
    }

    public void saveFarms() {
        for (Farm farm : farmGrid.getFarms()) {
            saveFarm(farm);
        }
    }

    public void loadFarms(ArrayList<PlayerData> playerData) {
      for (PlayerData p : playerData) {
            loadFarm(p);
      }
    }

    public boolean deleteFarm(PlayerData playerData) {
        if(playerData.getFarmIndex() == -1) {
            return false;
        }
        //cubefarm.getConfig().set("farms." + playerData.getFarmIndex(), null); //PROBLEM:
        //cubefarm.getConfig().getConfigurationSection("farms").set(""+ playerData.getFarmIndex(), null);
        getFarmFile(playerData.getFarmIndex()).delete();
        playerData.setFarmIndex(-1);
        cubefarm.saveConfig();
        return true;
    }
}
