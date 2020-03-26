package com.guillaumevdn.gslotmachine;

import java.io.File;
import java.util.List;

import com.guillaumevdn.gcore.lib.messenger.Text;
import com.guillaumevdn.gcore.lib.util.Utils;

public class GSMLocale {

	public static final File file = new File(GSlotMachine.inst().getDataFolder() + "/texts.yml");

	private static Text n(String id, Object... content) {
		return new Text(id, file, content);
	}

	private static List<String> l(String... content) {
		return Utils.asList(content);
	}

	public static final Text MSG_GSLOTMACHINE_INVALIDMACHINEPARAM = n(
			"MSG_GSLOTMACHINE_INVALIDMACHINEPARAM",
			"en_US", l("&6GSlotMachine >> &7Parameter &c{parameter} &7should be an existing machine."),
			"fr_FR", l("&6GSlotMachine >> &7Le paramètre &c{parameter} &7devrait être une machine existante."),
			"zh_TW", l("&6GSlotMachine >> &7參數 &c{parameter} &7須為存在的機器")
			);

	public static final Text MSG_GSLOTMACHINE_INVALIDMACHINETYPEPARAM = n(
			"MSG_GSLOTMACHINE_INVALIDMACHINETYPEPARAM",
			"en_US", l("&6GSlotMachine >> &7Parameter &c{parameter} &7should be an existing machine type."),
			"fr_FR", l("&6GSlotMachine >> &7Le paramètre &c{parameter} &7devrait être un type de machine existant."),
			"zh_TW", l("&6GSlotMachine >> &7參數 &c{parameter} &7須為存在的機器類型")
			);

	public static final Text MSG_GSLOTMACHINE_INVALIDBUTTON = n(
			"MSG_GSLOTMACHINE_INVALIDBUTTON",
			"en_US", l("&6GSlotMachine >> &7You're not pointing a valid button with your crosshair."),
			"fr_FR", l("&6GSlotMachine >> &7Vous ne pointez pas un bouton valide avec votre curseur."),
			"zh_TW", l("&6GSlotMachine >> &7你沒有指向任何有效的按鈕")
			);

	public static final Text MSG_GSLOTMACHINE_CREATE = n(
			"MSG_GSLOTMACHINE_CREATE",
			"en_US", l("&6GSlotMachine >> &7Machine &a{id} &7was successfully created."),
			"fr_FR", l("&6GSlotMachine >> &7La machine &a{id} &7a été créée avec succès."),
			"zh_TW", l("&6GSlotMachine >> &7機器 &a{id} &7成功設定")
			);

	public static final Text MSG_GSLOTMACHINE_SETBUTTON = n(
			"MSG_GSLOTMACHINE_SETBUTTON",
			"en_US", l("&6GSlotMachine >> &7Button was set for machine &a{machine}&7."),
			"fr_FR", l("&6GSlotMachine >> &7Le bouton a été défini pour la machine &a{machine}&7."),
			"zh_TW", l("&6GSlotMachine >> &7按鈕已設置為 &a{machine}&7的啟動按鈕")
			);

	public static final Text MSG_GSLOTMACHINE_SETCASE = n(
			"MSG_GSLOTMACHINE_SETCASE",
			"en_US", l("&6GSlotMachine >> &7Case &a{case} &7was set for machine &a{machine}&7."),
			"fr_FR", l("&6GSlotMachine >> &7La case &a{case} &7a été défini pour la machine &a{machine}&7."),
			"zh_TW", l("&6GSlotMachine >> &a{case} &7已設置為 &a{machine}&7機器使用")
			);

	public static final Text MSG_GSLOTMACHINE_WIN = n(
			"MSG_GSLOTMACHINE_WIN",
			"en_US", l("&6GSlotMachine >> &7You won &a{item}&7, congratulations !"),
			"fr_FR", l("&6GSlotMachine >> &7Vous avez gagné &a{item}&7, félicitations !"),
			"zh_TW", l("&6GSlotMachine >> &7你贏得 &a{item}&7, 恭喜 !")
			);

	public static final Text MSG_GSLOTMACHINE_LOSE = n(
			"MSG_GSLOTMACHINE_LOSE",
			"en_US", l("&6GSlotMachine >> &7Unluckily, you didn't win anything :("),
			"fr_FR", l("&6GSlotMachine >> &7Malheureusement, vous n'avez rien gagné :("),
			"zh_TW", l("&6GSlotMachine >> &7不幸的,你啥都沒贏到")
			);

}
