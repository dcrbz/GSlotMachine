package com.guillaumevdn.gslotmachine.commands;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.guillaumevdn.gslotmachine.GSMLocale;
import com.guillaumevdn.gslotmachine.GSMPerm;
import com.guillaumevdn.gslotmachine.GSlotMachine;
import com.guillaumevdn.gslotmachine.data.Machine;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.command.CommandArgument;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.material.Mat;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CommandSetcase extends CommandArgument {

	private static final Param paramMachine = new Param(Utils.asList("machine", "m"), "id", GSMPerm.GSLOTMACHINE_ADMIN, true, true);
	private static final Param paramCase = new Param(Utils.asList("case"), "id", GSMPerm.GSLOTMACHINE_ADMIN, true, true);

	public CommandSetcase() {
		super(GSlotMachine.inst(), Utils.asList("setcase"), "set a machine case", GSMPerm.GSLOTMACHINE_ADMIN, true, paramMachine, paramCase);
	}

	@Override
	public void perform(CommandCall call) {
		Player sender = call.getSenderAsPlayer();
		Machine machine = paramMachine.get(call, GSlotMachine.MACHINE_PARSER);
		int caseId = paramCase.getInt(call);
		if (machine != null && caseId != Integer.MIN_VALUE) {
			// block
			Block block = sender.getTargetBlock((Set<Material>) null, 5);
			if (block == null || Mat.fromBlock(block).isAir()) {
				GLocale.MSG_GENERIC_INVALIDCROSSHAIRBLOCK.send(sender, "{plugin}", GSlotMachine.inst().getName());
				return;
			}
			// set case
			Location loc = block.getLocation().clone().add(0.5D, 0.1D, 0.5D);
			machine.setCase(caseId, loc);
			GSMLocale.MSG_GSLOTMACHINE_SETCASE.send(sender, "{case}", caseId, "{machine}", machine.getId());
		}
	}

}
