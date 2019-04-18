package com.guillaumevdn.gslotmachine.data;

import com.guillaumevdn.gslotmachine.GSlotMachine;

import com.guillaumevdn.gcore.lib.data.DataManager;

public class PSMDataManager extends DataManager {

	// constructor
	private MachineBoard machineBoard = null;

	public PSMDataManager(BackEnd backend) {
		super(GSlotMachine.inst(), backend);
	}

	// getters
	public MachineBoard getMachines() {
		return machineBoard;
	}

	// methods
	@Override
	protected void innerEnable() {
		// machines
		this.machineBoard = new MachineBoard();
		machineBoard.initAsync(new Callback() { @Override public void callback() {
			machineBoard.pullAsync(null);
		}});
	}

	@Override
	protected void innerSynchronize() {
		machineBoard.pullAsync(null);
	}

	@Override
	protected void innerReset() {
		machineBoard.deleteAsync();
	}

	@Override
	protected void innerDisable() {
		this.machineBoard = null;
	}

}
