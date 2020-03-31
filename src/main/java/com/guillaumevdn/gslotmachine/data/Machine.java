package com.guillaumevdn.gslotmachine.data;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.guillaumevdn.gslotmachine.GSlotMachine;
import com.guillaumevdn.gslotmachine.machine.MachineType;
import com.guillaumevdn.gslotmachine.machine.RunningMachine;

import com.guillaumevdn.gcore.GCore;
import com.guillaumevdn.gcore.lib.data.DataElement;
import com.guillaumevdn.gcore.lib.data.mysql.Query;
import com.guillaumevdn.gcore.lib.util.Utils;

public class Machine extends DataElement {

	// fields and constructor
	private String id;
	private String type = null;
	private Map<Integer, Location> cases = new HashMap<Integer, Location>();
	private Location button;
	private transient RunningMachine running = null;

	public Machine(String id) {
		this.id = id;
	}

	public Machine(String id, MachineType type) {
		this.id = id;
		this.type = type.getId();
	}

	// getters
	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(MachineType type) {
		this.type = type.getId();
		pushAsync();
	}

	public Location getCase(int id) {
		return cases.containsKey(id) ? cases.get(id) : null;
	}

	public void setCase(int id, Location location) {
		cases.put(id, location);
		pushAsync();
	}

	public Collection<Location> getCases() {
		return cases.values();
	}

	public Location getButton() {
		return button;
	}

	public void setButton(Location button) {
		this.button = button;
		pushAsync();
	}

	public RunningMachine getRunning() {
		return running;
	}

	public void setRunningMachine(RunningMachine running) {
		this.running = running;
		pushAsync();
	}


	// ----------------------------------------------------------------------
	// Data
	// ----------------------------------------------------------------------

	@Override
	protected final MachineBoard getBoard() {
		return GSlotMachine.inst().getData().getMachines();
	}

	@Override
	protected String getDataId() {
		return id;
	}

	// JSON
	private static final class JsonData {
		private final String type;
		private final Map<Integer, Location> cases;
		private final Location button;
		private JsonData(Machine machine) {
			this.type = machine.type;
			this.cases = machine.cases;
			this.button = machine.button;
		}
	}

	protected final void jsonPull() {
		File file = getBoard().getJsonFile(this);
		if (file.exists()) {
			JsonData data = Utils.loadFromGson(JsonData.class, file, true);
			if (data != null) {
				// clear cache
				this.cases.clear();
				// replace
				this.type = data.type;
				this.cases.putAll(data.cases);
				this.button = data.button;
			}
		}
	}

	protected final void jsonPush() {
		File file = getBoard().getJsonFile(this);
		Utils.saveToGson(new JsonData(this), file);
	}

	protected final void jsonDelete() {
		File file = getBoard().getJsonFile(this);
		if (file.exists()) {
			file.delete();
		}
	}

	protected final void mysqlPull(ResultSet set) throws SQLException {
		// clear cache
		this.cases.clear();
		// replace
		this.type = set.getString("type");
		Map<String, String> cases = GCore.UNPRETTY_GSON.fromJson(set.getString("cases"), new HashMap<String, String>().getClass());
		for (String str : cases.keySet()) {
			this.cases.put(Integer.parseInt(str), Utils.unserializeWXYZLocation(cases.get(str)));
		}
		this.button = Utils.unserializeWXYZLocation(set.getString("button"));
	}

	protected final Query getMySQLPullQuery() {
		return new Query("SELECT * FROM `" + getBoard().getMySQLTable() + "` WHERE `id`=?;", getDataId());
	}

	protected final Query getMySQLPushQuery() {
		Map<String, String> copy = new HashMap<String, String>();
		for (int id : this.cases.keySet()) {
			copy.put(String.valueOf(id), Utils.serializeWXYZLocation(this.cases.get(id)));
		}
		return new Query("REPLACE INTO `" + getBoard().getMySQLTable() + "`(`id`,`type`,`cases`,`button`) VALUES(?,?,?,?);", id, type, GCore.UNPRETTY_GSON.toJson(copy), 
				Utils.serializeWXYZLocation(button));
	}

	protected final Query getMySQLDeleteQuery() {
		return new Query("DELETE FROM `" + getBoard().getMySQLTable() + "` WHERE `id`=?;", getDataId());
	}

}
