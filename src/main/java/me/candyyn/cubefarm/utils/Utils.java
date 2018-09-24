package me.candyyn.cubefarm.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

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
}
