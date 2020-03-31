package com.guillaumevdn.gslotmachine.util;

import org.bukkit.inventory.ItemStack;

public class MachineUtils {

	public static String describe(ItemStack item) {
		return item.getAmount() + "x " + item.getType().toString().toLowerCase().replace("_", " ") + (item.getData().getData() == (byte) 0 ? "" : ":" + item.getData().getData());
	}

}
