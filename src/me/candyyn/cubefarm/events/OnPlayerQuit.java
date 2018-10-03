package me.candyyn.cubefarm.events;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.manager.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OnPlayerQuit implements Listener {

    private CubeFarm cubeFarm = CubeFarm.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerDataManager playerDataManager = cubeFarm.getPlayerDataManager();

        // If the user have a farm, Save it
        if(cubeFarm.getFarmGrid().hasFarm(uuid)) {
            cubeFarm.getFarmManager().saveFarm(cubeFarm.getFarmGrid().getFarm(uuid));
        }

        // Save player data before exit
        playerDataManager.savePlayerData(uuid);

        // Remove player data from cache so it can be re-created on login
        playerDataManager.getPlayers().remove(uuid);
   }
        
}