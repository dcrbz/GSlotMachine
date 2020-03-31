package com.guillaumevdn.gslotmachine;

import java.io.File;

import com.guillaumevdn.gcore.lib.messenger.Text;

public class GSMLocale {

	// ----------------------------------------------------------------------
	// Fields
	// ----------------------------------------------------------------------

	public static final File file = new File(GSlotMachine.inst().getDataFolder() + "/texts.yml");

	// ----------------------------------------------------------------------
	// Messages
	// ----------------------------------------------------------------------
	
	public static final Text MSG_GSLOTMACHINE_INVALIDMACHINEPARAM = new Text(
			"MSG_GSLOTMACHINE_INVALIDMACHINEPARAM", file,
			"en_US", "&6GSlotMachine >> &7Parameter &c{parameter} &7should be an existing machine.",
			"fr_FR", "&6GSlotMachine >> &7Le paramètre &c{parameter} &7devrait être une machine existante."
			);

	public static final Text MSG_GSLOTMACHINE_INVALIDMACHINETYPEPARAM = new Text(
			"MSG_GSLOTMACHINE_INVALIDMACHINETYPEPARAM", file,
			"en_US", "&6GSlotMachine >> &7Parameter &c{parameter} &7should be an existing machine type.",
			"fr_FR", "&6GSlotMachine >> &7Le paramètre &c{parameter} &7devrait être un type de machine existant."
			);

	public static final Text MSG_GSLOTMACHINE_INVALIDBUTTON = new Text(
			"MSG_GSLOTMACHINE_INVALIDBUTTON", file,
			"en_US", "&6GSlotMachine >> &7You're not pointing a valid button with your crosshair.",
			"fr_FR", "&6GSlotMachine >> &7Vous ne pointez pas un bouton valide avec votre curseur."
			);

	public static final Text MSG_GSLOTMACHINE_CREATE = new Text(
			"MSG_GSLOTMACHINE_CREATE", file,
			"en_US", "&6GSlotMachine >> &7Machine &a{id} &7was successfully created.",
			"fr_FR", "&6GSlotMachine >> &7La machine &a{id} &7a été créée avec succès."
			);

	public static final Text MSG_GSLOTMACHINE_SETBUTTON = new Text(
			"MSG_GSLOTMACHINE_SETBUTTON", file,
			"en_US", "&6GSlotMachine >> &7Button was set for machine &a{machine}&7.",
			"fr_FR", "&6GSlotMachine >> &7Le bouton a été défini pour la machine &a{machine}&7."
			);

	public static final Text MSG_GSLOTMACHINE_SETCASE = new Text(
			"MSG_GSLOTMACHINE_SETCASE", file,
			"en_US", "&6GSlotMachine >> &7Case &a{case} &7was set for machine &a{machine}&7.",
			"fr_FR", "&6GSlotMachine >> &7La case &a{case} &7a été défini pour la machine &a{machine}&7."
			);

	public static final Text MSG_GSLOTMACHINE_WIN = new Text(
			"MSG_GSLOTMACHINE_WIN", file,
			"en_US", "&6GSlotMachine >> &7You won &a{item}&7, congratulations !",
			"fr_FR", "&6GSlotMachine >> &7Vous avez gagné &a{item}&7, félicitations !"
			);

	public static final Text MSG_GSLOTMACHINE_LOSE = new Text(
			"MSG_GSLOTMACHINE_LOSE", file,
			"en_US", "&6GSlotMachine >> &7Unluckily, you didn't win anything :(",
			"fr_FR", "&6GSlotMachine >> &7Malheureusement, vous n'avez rien gagné :("
			);
	
}
