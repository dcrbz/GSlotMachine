package com.guillaumevdn.gslotmachine.machine;

import java.util.List;

import com.guillaumevdn.gcore.lib.gui.ItemData;

public class MachinePrize {

	// base
	private ItemData item;
	private List<String> commands;
	private boolean giveItem;

	public MachinePrize(ItemData item, List<String> commands, boolean giveItem) {
		this.item = item;
		this.commands = commands;
		this.giveItem = giveItem;
	}

	// get
	public ItemData getItem() {
		return item;
	}

	public List<String> getCommands() {
		return commands;
	}

	public boolean getGiveItem() {
		return giveItem;
	}

}
