package com.guillaumevdn.gslotmachine.machine;

import java.util.List;

import com.guillaumevdn.gcore.lib.versioncompat.sound.Sound;

public class MachineType {

	// fields and constructor
	private String id;
	private double cost;
	private Sound animationSound, winSound, loseSound;
	private List<MachinePrize> prizes;

	public MachineType(String id, double cost, Sound animationSound, Sound winSound, Sound loseSound, List<MachinePrize> prizes) {
		this.id = id;
		this.cost = cost;
		this.animationSound = animationSound;
		this.winSound = winSound;
		this.loseSound = loseSound;
		this.prizes = prizes;
	}

	// getters
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return getId();
	}

	public double getCost() {
		return cost;
	}

	public Sound getAnimationSound() {
		return animationSound;
	}

	public Sound getWinSound() {
		return winSound;
	}

	public Sound getLoseSound() {
		return loseSound;
	}

	public List<MachinePrize> getPrizes() {
		return prizes;
	}

}
