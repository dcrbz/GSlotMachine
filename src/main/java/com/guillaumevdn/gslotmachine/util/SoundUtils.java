package com.guillaumevdn.gslotmachine.util;

import com.guillaumevdn.gslotmachine.GSlotMachine;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static Sound fromString(String str, Sound def) {
        try {
            return Sound.valueOf(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static void play(String sound, OfflinePlayer offlinePlayer, float volume, float pitch) {
        Player player = offlinePlayer.getPlayer();

        if (player == null) {
            return;
        }

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}
