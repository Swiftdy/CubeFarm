package me.candyyn.cubefarm.utils;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Utils {

    public static Location deserializeLocation(String s) {
        String[] split = s.split("::");
        return new Location(Bukkit.getWorld(split[0]), Double.valueOf(split[1]),
                Double.valueOf(split[2]), Double.valueOf(split[3]),
                Float.valueOf(split[4]), Float.valueOf(split[5]));
    }

    public static String serializeLocation(Location loc) {
        String builder = loc.getWorld().getName() +
                "::" + loc.getX() + "::" + loc.getY() + "::" + loc.getZ() +
                "::" + loc.getYaw() + "::" + loc.getPitch();
        return builder.trim();
    }


    public LuckPermsApi getLPApi() {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        LuckPermsApi api = null;
        if (provider != null) {
            api = provider.getProvider();
        }
        return api;
    }

    public void setUserMainGroup(UUID uuid, String group) {
        UserManager userManager = getLPApi().getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uuid);
        userFuture.thenAcceptAsync(user -> {
            user.setPrimaryGroup(group);
        });
    }

}
