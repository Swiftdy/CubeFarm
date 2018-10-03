package me.candyyn.cubefarm.events.enchant;

import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.Farm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.farm.FarmSection;
import me.candyyn.cubefarm.manager.ChatManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OnInventoryClick implements Listener {

    final EditSession session = CubeFarm.getWorldManager().getEditSession();

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent  e) {
        Inventory inventory = e.getClickedInventory();
        if (inventory != null) {
            if (e.getClickedInventory().getType() == InventoryType.MERCHANT) {
                if (e.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
                    if (e.getClickedInventory().getItem(0).getItemMeta().getDisplayName().contains("Chunk")) {
                        // Format book title
                        String cords = e.getClickedInventory().getItem(0).getItemMeta().getDisplayName().replaceAll("[^0-9,]", "");
                        String[] cord = cords.split(",");


                        List<String> lore =  e.getClickedInventory().getItem(0).getItemMeta().getLore();

                        if(!lore.get(1).contains(e.getWhoClicked().getName())) {
                            e.setCancelled(true);
                            ChatManager.sendMessage(Bukkit.getPlayer(e.getWhoClicked().getUniqueId()), "This Lease isn't signed to you.", ChatColor.RED);
                            return;
                        }

                        // Get farm section from book title
                        FarmSection section = CubeFarm.getFarmGrid().getFarm(e.getWhoClicked().getUniqueId()).getFarmSection(Integer.parseInt(cord[0]), Integer.parseInt(cord[1]));

                        // TODO: Dynamic replacement
                        section.schematicGroupName = "grass";
                        section.schematicSlot = 0;

                        // Update section
                        TaskManager.IMP.async(() -> {
                            section.generate(session);
                            session.flushQueue();
                        });

                        // Lease message
                        ChatManager.sendMessage(Bukkit.getPlayer(e.getWhoClicked().getUniqueId()), "You just leased " + e.getClickedInventory().getItem(0).getItemMeta().getDisplayName(), ChatColor.BLUE);

                        // Play sound for chunk
                        Bukkit.getPlayer(e.getWhoClicked().getUniqueId()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER, 1f, 1);

                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
