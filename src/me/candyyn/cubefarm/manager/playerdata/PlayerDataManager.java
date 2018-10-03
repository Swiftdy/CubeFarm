package me.candyyn.cubefarm.manager.playerdata;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerDataManager {
    private Map<UUID, PlayerData> players;

    private CubeFarm cubefarm;

    public PlayerDataManager (CubeFarm cubeFarm) {
        this.cubefarm = cubeFarm;
        players = new HashMap<>();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return players.get(uuid);
    }

    public void addPlayerData(PlayerData data) {
        players.put(data.getUUID(), data);
    }

    public PlayerData createPlayerData(UUID uuid) {
        PlayerData playerData = new PlayerData(uuid);
        players.put(uuid, playerData);
        return playerData;
    }


    public Map<UUID, PlayerData> getPlayers() {
        return players;
    }

    public PlayerData loadPlayerData(UUID uuid) {
        ConfigurationSection section = cubefarm.getConfig().getConfigurationSection("players." + uuid);
        PlayerData playerData = createPlayerData(uuid);
        int farmIndex = section.getInt("owned_farm"); // returns 0
        int balance = section.getInt("money");
        Bukkit.getLogger().warning("Config for: " + uuid + " Owned farm is equal to: " + farmIndex);
        playerData.setFarmIndex(farmIndex);
        playerData.setBalance(balance);
        Bukkit.getLogger().info("loadPlayerData: " + playerData + " farmIndex: " + farmIndex + "uuid: " + uuid);
        return playerData;
    }

    public boolean doesExist(UUID uuid) {
        return players.get(uuid) != null; // remove the player from the Player list
    }

    public boolean doesExistConfig(UUID uuid) {
        return cubefarm.getConfig().get("players." + uuid) != null;
    }

    public ArrayList<PlayerData> loadPlayersData() {
        ArrayList<PlayerData> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(loadPlayerData(player.getUniqueId()));
        }
        return players;
    }

    public void savePlayersData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player.getUniqueId());
        }
    }

    public void savePlayerData(UUID uuid) {
        PlayerData playerData = getPlayerData(uuid);
        ConfigurationSection section;
        if(cubefarm.getConfig().getString("players." + uuid) == null ) {
            section = cubefarm.getConfig().createSection("players." + uuid);
        }else {
            section = cubefarm.getConfig().getConfigurationSection("players." + uuid);
        }
        section.set("owned_farm", playerData.getFarmIndex());
        section.set("money", 100);
        cubefarm.saveConfig();
    }
}
