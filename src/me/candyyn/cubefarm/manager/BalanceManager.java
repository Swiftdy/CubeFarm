package me.candyyn.cubefarm.manager;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.manager.playerdata.PlayerData;
import me.candyyn.cubefarm.manager.playerdata.PlayerDataManager;

import java.util.UUID;

public class BalanceManager {

    private CubeFarm cubeFarm;
    private PlayerDataManager playerDataManager;
    private WorldManager worldManager;

    public BalanceManager(CubeFarm cubeFarm, PlayerDataManager playerDataManager, WorldManager worldManager) {
        this.cubeFarm = cubeFarm;
        this.playerDataManager = playerDataManager;
        this.worldManager = worldManager;
    }

    public void setBalance(UUID uuid, int amount) {
        PlayerData playerData = playerDataManager.getPlayerData(uuid);
        playerData.setFarmIndex(amount);
    }

    public int getBalance(UUID uuid) {
        PlayerData playerData = playerDataManager.getPlayerData(uuid);
        if(playerData != null) {
            int balance = playerData.getBalance();
            return balance;
        }
        return -1;
    }
}
