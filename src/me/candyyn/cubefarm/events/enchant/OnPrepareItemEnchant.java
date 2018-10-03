package me.candyyn.cubefarm.events.enchant;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.utils.Constants;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftVillager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnPrepareItemEnchant implements Listener {

    final ItemStack ink = new ItemStack(Material.LAPIS_LAZULI);

    private CubeFarm cubeFarm = CubeFarm.getInstance();

    public void update(Player p, String title)
    {
        EntityPlayer ep = ((CraftPlayer)p).getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, "minecraft:enchanting_table", new ChatMessage(title), p.getOpenInventory().getTopInventory().getSize());
        ep.playerConnection.sendPacket(packet);
        ep.updateInventory(ep.activeContainer);
    }

    @EventHandler
    public void onPreareItemEnchant(PrepareItemEnchantEvent event) {

        /*EnchantmentOffer[] offers = event.getOffers();
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        Block block = event.getEnchantBlock();

        Inventory inventory = event.getInventory();

        if(event.getItem().getType() == Material.BOOK) {
            event.setCancelled(false);
            NamespacedKey key = new NamespacedKey(cubeFarm, "buy_land");
            offers[0] = new EnchantmentOffer(new BuylandEnchantment(key), 1, 1);
        }
       // offers[0].setEnchantment();
        //update(player, "Lease");
       // Enchantment.ARROW_FIRE = new EnchantmentWrapper("buy_land");
        //offers[0].setEnchantment(enchantment_buy);
        */
    }


    @EventHandler
    public void openInventoryEvent(InventoryOpenEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.setCancelled(true);

            WorldServer world = ((CraftWorld) e.getPlayer().getWorld()).getHandle();
            EntityVillager entityvillager = new EntityVillager(world);
            CraftVillager villager = new CraftVillager(null, entityvillager);
            villager.setCustomName("Lease");
            villager.setGravity(true);
            villager.teleport(new Location(e.getPlayer().getWorld(), 0, 0, 0));
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.ENCHANTED_BOOK), 10000); // no max-uses limit
            recipe.setExperienceReward(false); // no experience rewards
            recipe.addIngredient(new ItemStack(Material.WRITABLE_BOOK));
            recipe.addIngredient(new ItemStack(Material.INK_SAC));
            villager.setRecipe(0, recipe);
            List<MerchantRecipe> recipeList = new ArrayList<MerchantRecipe>();
            recipeList.add(recipe);
            if(recipeList != null) {
                villager.setRecipes(recipeList);

                e.getPlayer().openMerchant(villager, true);
            }

        }
    }
}
