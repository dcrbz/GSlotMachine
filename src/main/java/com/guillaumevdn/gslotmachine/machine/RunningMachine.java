package com.guillaumevdn.gslotmachine.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.guillaumevdn.gslotmachine.util.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.guillaumevdn.gslotmachine.GSMLocale;
import com.guillaumevdn.gslotmachine.GSlotMachine;
import com.guillaumevdn.gslotmachine.data.Machine;
import com.guillaumevdn.gslotmachine.util.MachineUtils;

import com.guillaumevdn.gcore.GCore;
import com.guillaumevdn.gcore.lib.material.Mat;
import com.guillaumevdn.gcore.lib.util.Utils;
import com.guillaumevdn.gcore.lib.versioncompat.particle.ParticleManager;
import com.guillaumevdn.gcore.lib.versioncompat.sound.Sound;

public class RunningMachine {

	private static final float SOUND_VOLUME = 0.5f;
	private static final float SOUND_PITCH = 1.0f;

	// fields and constructor
	private OfflinePlayer player;
	private Machine machine;
	private MachineType machineType;
	private List<MachineItem> results = new ArrayList<>();
	private MachineItem tend1, tend2, tend3, result;
	private int taskId = -1;
	private Map<Integer, Item> actualItems = new HashMap<>();

	public RunningMachine(OfflinePlayer player, Machine machine, MachineType machineType) {
		this.player = player;
		this.machine = machine;
		this.machineType = machineType;
		// precalculate prize chances
		results.addAll(machineType.getPrizes());
		// precalculate result
		List<MachineItem> chances = new ArrayList<>();
		for (MachineItem it : machineType.getPrizes()) {
			for (int i = 0; i < it.getChance(); i++) {
				chances.add(it);
			}
		}
		while (chances.size() < 100) {
			chances.add(null);
		}
		result = Utils.random(chances);
		// open cases
		for (Location loc : machine.getCases()) {
			Mat.AIR.setBlock(loc.getBlock());
		}
		// tend
		tend1 = result == null ? Utils.random(results) : result;
		tend2 = result == null ? Utils.random(results) : result;
		tend3 = result == null ? Utils.random(results) : result;
	}

	// start
	public void start() {
		// take money
		GCore.inst().getEconomyHandler().take(player, machineType.getCost());
		// play spin sound
		SoundUtils.play(machineType.getSpinSound(), player, SOUND_VOLUME, SOUND_PITCH);
		// start
		taskId = new BukkitRunnable() {
			private long delay = 2L, currentDelay = 0L, totalDuration = 0L;
			@Override
			public void run() {
				currentDelay += 1L;
				totalDuration += 1L;
				// stop
				if (totalDuration >= 260L) {
					stop(true);
					return;
				}
				// change delay or items
				if (currentDelay >= delay) {
					currentDelay = 0L;
					// change delay
					if (totalDuration >= 220L) delay = 20L;
					else if (totalDuration >= 180L) delay = 15L;
					else if (totalDuration >= 140L) delay = 10L;
					else if (totalDuration >= 100L) delay = 5L;
					else if (totalDuration >= 60L) delay = 3L;
					// set items
					setCase(1, totalDuration >= 180L ? tend1 : Utils.random(results));
					setCase(2, totalDuration >= 210L ? tend2 : Utils.random(results));
					setCase(3, totalDuration >= 240L ? tend3 : Utils.random(results));
					// sound
					if (machineType.getAnimationSound() != null) SoundUtils.play(machineType.getAnimationSound(), player, SOUND_VOLUME, SOUND_PITCH);
				}
			}
		}.runTaskTimer(GSlotMachine.inst(), 0L, 1L).getTaskId();
	}

	private void setCase(int id, MachineItem item) {
		Location loc = machine.getCase(id);
		if (loc != null) {
			// remove previous
			if (actualItems.containsKey(id)) {
				actualItems.get(id).remove();
			}
			// add new
			ItemStack itm = item.getItemStack();
			Item it = loc.getWorld().dropItem(loc, itm);
			it.setPickupDelay(Integer.MAX_VALUE);
			it.setVelocity(new Vector(0, 0, 0));
			actualItems.put(id, it);
			// particle effect
			ParticleManager.INSTANCE.send(ParticleManager.Type.CLOUD, it.getLocation(), 0.0F, 1, Utils.asList(loc.getWorld().getPlayers()));
		}
	}

	// stop
	public void stop(boolean succeed) {
		// succeed
		if (succeed && player.isOnline()) {
			Player player = this.player.getPlayer();
			// loose
			if (result == null) {
				GSMLocale.MSG_GSLOTMACHINE_LOSE.send(player);
				// play sound
				if (machineType.getLoseSound() != null) SoundUtils.play(machineType.getLoseSound(), player, SOUND_VOLUME, SOUND_PITCH);
			}
			// win
			else {
				// give item
				result.give(player);
				// execute commands
				Bukkit.getScheduler().runTask(GSlotMachine.inst(), () -> {
					for (String cmd : result.getCommands()) {
						cmd = cmd.replace("{player}", player.getName());
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
					}
				});
				// message
				GSMLocale.MSG_GSLOTMACHINE_WIN.send(player, "{item}", MachineUtils.describe(result.getItemStack()));
				// play sound
				if (machineType.getWinSound() != null) SoundUtils.play(machineType.getWinSound(), player, SOUND_VOLUME, SOUND_PITCH);
			}
		}
		// fail, so refund
		else {
			GCore.inst().getEconomyHandler().give(player, machineType.getCost());
		}
		// clear items
		for (Item item : actualItems.values()) {
			item.remove();
		}
		actualItems.clear();
		// close cases
		for (Location loc : machine.getCases()) {
			Mat.GLASS.setBlock(loc.getBlock());
		}
		// cancel task
		Bukkit.getScheduler().cancelTask(taskId);
		// set not running
		machine.setRunningMachine(null);
	}

}
