package com.guillaumevdn.gslotmachine.commands;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.guillaumevdn.gslotmachine.GSMLocale;
import com.guillaumevdn.gslotmachine.GSMPerm;
import com.guillaumevdn.gslotmachine.GSlotMachine;
import com.guillaumevdn.gslotmachine.data.Machine;

import com.guillaumevdn.gcore.lib.command.CommandArgument;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CommandSetbutton extends CommandArgument {

	private static final Param paramMachine = new Param(Utils.asList("machine", "m"), "id", GSMPerm.GSLOTMACHINE_ADMIN, true, true);

	public CommandSetbutton() {
		super(GSlotMachine.inst(), Utils.asList("setbutton"), "set the machine button", GSMPerm.GSLOTMACHINE_ADMIN, true, paramMachine);
	}

	@Override
	public void perform(CommandCall call) {
		Player sender = call.getSenderAsPlayer();
		Machine machine = paramMachine.get(call, GSlotMachine.MACHINE_PARSER);
		if (machine != null) {
			// invalid button
			Block block = sender.getTargetBlock(null, 5);
			if (block == null || !block.getType().toString().contains("BUTTON")) {
				GSMLocale.MSG_GSLOTMACHINE_INVALIDBUTTON.send(sender);
				return;
			}
			// set button
			machine.setButton(block.getLocation());
			GSMLocale.MSG_GSLOTMACHINE_SETBUTTON.send(sender, "{machine}", machine.getId());
		}
	}

}
