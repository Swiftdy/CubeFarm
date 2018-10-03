package me.candyyn.cubefarm.commands;

import me.candyyn.cubefarm.CubeFarm;
import me.candyyn.cubefarm.farm.FarmGrid;
import me.candyyn.cubefarm.manager.BalanceManager;
import me.candyyn.cubefarm.manager.FarmManager;
import me.candyyn.cubefarm.manager.SchematicManager;
import me.candyyn.cubefarm.manager.WorldManager;
import me.candyyn.cubefarm.manager.playerdata.PlayerDataManager;
import me.candyyn.cubefarm.schematic.SchematicGrid;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class BalanceCommand implements CommandExecutor {

    private WorldManager worldManager;
    private CubeFarm cubeFarm;
    private PlayerDataManager playerDataManager;
    private BalanceManager balanceManager;
    private Logger log = Bukkit.getLogger();

    public BalanceCommand(CubeFarm cubeFarm, WorldManager worldManager,  PlayerDataManager playerDataManager, BalanceManager balanceManager) {
        this.worldManager = worldManager;
        this.cubeFarm = cubeFarm;
        this.playerDataManager = playerDataManager;
        this.balanceManager = balanceManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            log.info("Command not supported for console");
            return true;
        }

        if(args.length < 1) {
            balanceManager.getBalance(((Player) commandSender).getUniqueId());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                break;
            case "send":
                break;
            case "logs":
                //TODO: Display the last 24h transactions
                break;
            //Admin
            case "admin":
                if(args.length > 1) {
                    switch (args[1].toLowerCase()) {
                        case "balance":
                            if (args.length > 2) {
                                //TODO: Display the given player Balance
                            }
                            break;
                        case "set":
                            //Todo: Allow to set a player balance
                            break;
                        case "reset":
                            //TODO: Allow to reset a player balance
                            break;
                        case "log":
                            //TODO: See the players last transactions
                            break;
                    }
                }
        }





        return true;
    }
}
