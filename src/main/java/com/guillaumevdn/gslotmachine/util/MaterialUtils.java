package com.guillaumevdn.gslotmachine.util;

import org.bukkit.Material;

public class MaterialUtils {

    private MaterialUtils() {}


    public static boolean isAir(Material material) {
        return material == Material.AIR || material == Material.CAVE_AIR || material == Material.VOID_AIR;
    }

    public static Material fromString(String str) {
        try {
            return Material.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

}
