package me.candyyn.cubefarm.manager.playerdata;

import me.candyyn.cubefarm.farm.Farm;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private int farmIndex = -1;
    private String rank = "Default";
    private int balance;

    public PlayerData(Player player) {
        this(player.getUniqueId());
    }
    
    public PlayerData(UUID uuid) {
        Bukkit.getLogger().info("PlayerData Created for: " + uuid);
        this.uuid = uuid;
    }


    public void setBalance(int amount) {
        this.balance = amount;
    }

    public void setFarmIndex(int index) {
        //Bukkit.getLogger().info("Im setting Farm index to: " + index + " For: " + uuid);
        this.farmIndex = index;
    }
    
    public int getFarmIndex() {
        return farmIndex;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getBalance() {
        return this.balance;
    }
}
