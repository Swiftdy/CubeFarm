package me.candyyn.cubefarm.events;


import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.manager.ChatManager;
import me.candyyn.cubefarm.manager.FarmManager;
import me.candyyn.cubefarm.manager.playerdata.PlayerData;
import me.candyyn.cubefarm.manager.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoin implements Listener {

    private CubeFarm cubeFarm = CubeFarm.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Bukkit.getLogger().info("A player has joined with the UUID " + uuid.toString());
        PlayerDataManager playerDataManager = cubeFarm.getPlayerDataManager();
        PlayerData playerData = null;

        // If the player has no data in the config, create player data in config.
        if (!playerDataManager.doesExistConfig(uuid)) {// if not in config
            Bukkit.getLogger().info("Creating new player data for player " + uuid.toString());
            playerData = playerDataManager.createPlayerData(uuid);

        }
        // Check if player data is in cache, if it isn't create the data.
        if (!playerDataManager.doesExist(player.getUniqueId())) { // if not cache
            playerData = playerDataManager.loadPlayerData(uuid);


            // If the data is null kick the player.
            if (playerData == null) {
                Bukkit.getLogger().warning("A player was kicked as their player data couldn't load. UUID: " + uuid.toString());
                player.kickPlayer("Your player data couldn't load");
                return;
            }

            // Check if the player has a farm, if they do load it in
            if (cubeFarm.getFarmManager().hasFarm(playerData)) {
                Bukkit.getLogger().info("Loading farm for player " + playerData.getUUID().toString() + " at index " + playerData.getFarmIndex());
                cubeFarm.getFarmManager().loadFarm(playerData);
            }

            // Save player data
            playerDataManager.savePlayerData(uuid);
        }
    }
}
