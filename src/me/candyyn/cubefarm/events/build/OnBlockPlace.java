package me.candyyn.cubefarm.events.build;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.farm.FarmSection;
import me.candyyn.cubefarm.manager.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnBlockPlace implements Listener {

    private CubeFarm cubeFarm = CubeFarm.getInstance();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = ((Block) block).getLocation();
        Farm playerFarm = CubeFarm.getFarmGrid().getFarm(player.getUniqueId());
        Farm placedFarm = CubeFarm.getFarmGrid().getFarm(location);
        if(placedFarm != null) {
            FarmSection section = CubeFarm.getFarmGrid().getSection(placedFarm, location);

            if (!placedFarm.getPlayerData().getUUID().equals(playerFarm.getPlayerData().getUUID())) {
                //TODO: Permission system
                ChatManager.sendMessage(player, "You cannot build on other players farms.", ChatColor.RED);
                event.setCancelled(true);
                return;
            }
            if (!placedFarm.isBuildableSection(section.getLCX(), section.getLCY())) {
                ChatManager.sendMessage(player, "You cannot build here as it's an unclaimed chunk", ChatColor.RED);
                event.setCancelled(true);
                return;
            }
        }
    }


}
