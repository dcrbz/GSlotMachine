package com.guillaumevdn.gslotmachine.machine;

import java.util.List;

public class MachineType {

	// fields and constructor
	private String id;
	private double cost;
	private String spinSound, animationSound, winSound, loseSound;
	private List<MachineItem> prizes;

	public MachineType(String id, double cost, String spinSound, String animationSound, String winSound, String loseSound, List<MachineItem> prizes) {
		this.id = id;
		this.cost = cost;
		this.spinSound = spinSound;
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

	public String getSpinSound() {
		return spinSound;
	}

	public String getAnimationSound() {
		return animationSound;
	}

	public String getWinSound() {
		return winSound;
	}

	public String getLoseSound() {
		return loseSound;
	}

	public List<MachineItem> getPrizes() {
		return prizes;
	}

}
