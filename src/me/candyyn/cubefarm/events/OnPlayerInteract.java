package me.candyyn.cubefarm.events;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.farm.FarmSection;
import me.candyyn.cubefarm.manager.ChatManager;
import me.candyyn.cubefarm.utils.ChunkAccessibilityType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class OnPlayerInteract implements Listener {

    CubeFarm cubeFarm = CubeFarm.getInstance();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Action action = event.getAction();
        Player player = event.getPlayer();

        if(action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Location location = block.getLocation();
            FarmGrid farmGrid = CubeFarm.getFarmGrid();

            // TODO: Make writable book an actual book system for leasing
            if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WRITABLE_BOOK) {
                //PlayerData playerData = CubeFarm.getPlayerDataManager().getPlayerData(player.getUniqueId());

                // Get farm via location of block
                Farm farm = farmGrid.getFarm(location);

                final String boundsErr = "The clicked block is not within the bounds of your farm.";
                if(farm == null) {
                    ChatManager.sendMessage(player, boundsErr + " (Err: 0x001)", ChatColor.RED);
                    return;
                }
                FarmSection section = farmGrid.getSection(farm, location);
                if(section == null) {
                    ChatManager.sendMessage(player, boundsErr + " (Err: 0x001)", ChatColor.RED);
                    return;
                }
                // Get the raw X, Y from the found chunk
                int sectionX = section.getLCX(); // For some reason for Elliot its 35 instead of
                int sectionY = section.getLCY();
                if(!farm.getPlayerData().getUUID().equals(player.getUniqueId())) {
                    ChatManager.sendMessage(player, "You can only lease within your own farm.", ChatColor.RED);
                    return;
                }

                if(section.schematicGroupName.contains("trees")) {
                   ChunkAccessibilityType accessibilityType = farm.isChunkAccessible(sectionX, sectionY);
                    switch(accessibilityType) {
                        case UNREACHABLE:
                            ChatManager.sendMessage(player, "Your farm needs to be connected to this chunk", ChatColor.RED);
                            return;
                        case EMPTY:
                            ChatManager.sendMessage(player, "You cant lease the bounds of your farm", ChatColor.RED);
                            return;
                    }

                    // Switch was not called due to correctness
                    ItemMeta meta = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    lore.add("Signed to " + event.getPlayer().getDisplayName());
                    meta.setLore(lore);
                    meta.setDisplayName("Chunk " + sectionX + ", " + sectionY);
                    meta.setLocalizedName("Chunk " + sectionX + ", " + sectionY);
                    event.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
                    ChatManager.sendMessage(player, "Selected chunk " + sectionX + ", " + sectionY + " to lease.", ChatColor.BLUE);
                } else {
                    ChatManager.sendMessage(player, "You can only lease unclaimed chunks.", ChatColor.RED);
                    return;
                }
            }

                    //event.getPlayer().getInventory().getItemInMainHand().getItemMeta().setDisplayName("Chunk");
                    //event.getPlayer().getInventory().getItemInMainHand().getItemMeta().setDisplayName("Chunk " + (section.getX() / 8) + ", " + (section.getY() / 8));
        }
    }
}


