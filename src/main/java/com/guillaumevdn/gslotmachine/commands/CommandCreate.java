package com.guillaumevdn.gslotmachine.commands;

import org.bukkit.entity.Player;

import com.guillaumevdn.gslotmachine.GSMLocale;
import com.guillaumevdn.gslotmachine.GSMPerm;
import com.guillaumevdn.gslotmachine.GSlotMachine;
import com.guillaumevdn.gslotmachine.data.Machine;
import com.guillaumevdn.gslotmachine.machine.MachineType;

import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.command.CommandArgument;
import com.guillaumevdn.gcore.lib.command.CommandCall;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.util.Utils;

public class CommandCreate extends CommandArgument {

	private static final Param paramMachine = new Param(Utils.asList("machine", "m"), "id", GSMPerm.GSLOTMACHINE_ADMIN, true, true);
	private static final Param paramType = new Param(Utils.asList("type", "t"), "id", GSMPerm.GSLOTMACHINE_ADMIN, true, true);

	public CommandCreate() {
		super(GSlotMachine.inst(), Utils.asList("create", "new"), "create a machine", GSMPerm.GSLOTMACHINE_ADMIN, true, paramMachine, paramType);
	}

	@Override
	public void perform(CommandCall call) {
		Player sender = call.getSenderAsPlayer();
		String id = paramMachine.getStringAlphanumeric(call);
		MachineType type = paramType.get(call, GSlotMachine.MACHINETYPE_PARSER);
		if (id != null && type != null) {
			// already taken
			if (GSlotMachine.inst().getData().getMachines().getElement(id) != null) {
				GLocale.MSG_GENERIC_IDTAKEN.send(sender, "{plugin}", GSlotMachine.inst().getName(), "{name}", id);
				return;
			}
			// create
			Machine machine = new Machine(id, type);
			GSlotMachine.inst().getData().getMachines().add(machine);
			GSMLocale.MSG_GSLOTMACHINE_CREATE.send(sender, "{id}", id);
		}
	}

}
