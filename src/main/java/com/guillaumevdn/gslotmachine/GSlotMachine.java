package com.guillaumevdn.gslotmachine;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import com.guillaumevdn.gslotmachine.machine.MachineItem;
import com.guillaumevdn.gslotmachine.util.MaterialUtils;
import com.guillaumevdn.gslotmachine.util.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.guillaumevdn.gcore.GCore;
import com.guillaumevdn.gcore.GLocale;
import com.guillaumevdn.gcore.lib.GPlugin;
import com.guillaumevdn.gcore.lib.command.CommandRoot;
import com.guillaumevdn.gcore.lib.command.Param;
import com.guillaumevdn.gcore.lib.command.ParamParser;
import com.guillaumevdn.gcore.lib.configuration.YMLConfiguration;
import com.guillaumevdn.gcore.lib.data.DataManager.BackEnd;
import com.guillaumevdn.gcore.lib.messenger.Messenger;
import com.guillaumevdn.gcore.lib.messenger.Messenger.Level;
import com.guillaumevdn.gcore.lib.util.Utils;
import com.guillaumevdn.gslotmachine.commands.CommandCreate;
import com.guillaumevdn.gslotmachine.commands.CommandSetbutton;
import com.guillaumevdn.gslotmachine.commands.CommandSetcase;
import com.guillaumevdn.gslotmachine.data.Machine;
import com.guillaumevdn.gslotmachine.data.PSMDataManager;
import com.guillaumevdn.gslotmachine.machine.MachineType;
import com.guillaumevdn.gslotmachine.machine.RunningMachine;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GSlotMachine extends GPlugin implements Listener {

    // ----------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------

    private static GSlotMachine instance;

    public GSlotMachine() {
        instance = this;
    }

    public static GSlotMachine inst() {
        return instance;
    }

    // ----------------------------------------------------------------------
    // Fields
    // ----------------------------------------------------------------------

    // static fields
    public static final ParamParser<Machine> MACHINE_PARSER = new ParamParser<Machine>() {
        @Override
        public Machine parse(CommandSender sender, Param parameter, String value) {
            // doesn't exist
            Machine machine = GSlotMachine.inst().getData().getMachines().getElement(value);
            if (machine == null) {
                GSMLocale.MSG_GSLOTMACHINE_INVALIDMACHINEPARAM.send(sender, "{parameter}", "-" + parameter.toString() + (parameter.getDescription() == null ? "" : ":" + value));
                return null;
            }
            // exists
            return machine;
        }
    };

    public static final ParamParser<MachineType> MACHINETYPE_PARSER = new ParamParser<MachineType>() {
        @Override
        public MachineType parse(CommandSender sender, Param parameter, String value) {
            // doesn't exist
            MachineType machine = GSlotMachine.inst().getMachineType(value);
            if (machine == null) {
                GSMLocale.MSG_GSLOTMACHINE_INVALIDMACHINETYPEPARAM.send(sender, "{parameter}", "-" + parameter.toString() + (parameter.getDescription() == null ? "" : ":" + value));
                return null;
            }
            // exists
            return machine;
        }
    };

    // fields
    private Map<String, MachineType> types = new HashMap<String, MachineType>();

    public MachineType getMachineType(String type) {
        type = type.toLowerCase();
        return types.containsKey(type) ? types.get(type) : null;
    }

    // ------------------------------------------------------------
    // Data and configuration
    // ------------------------------------------------------------

    private PSMDataManager dataManager = null;
    private YMLConfiguration configuration = null;

    @Override
    public YMLConfiguration getConfiguration() {
        return configuration;
    }

    public PSMDataManager getData() {
        return dataManager;
    }

    @Override
    protected void unregisterData() {
        dataManager.disable();
    }

    @Override
    public void resetData() {
        dataManager.reset();
    }

    // ------------------------------------------------------------
    // Activation
    // ------------------------------------------------------------

    @Override
    protected boolean preEnable() {
        // spigot resource id
        this.spigotResourceId = 55107;
        // success
        return true;
    }

    // ----------------------------------------------------------------------
    // Override : reload
    // ----------------------------------------------------------------------

    @Override
    protected boolean innerReload() {
        // configuration
        this.configuration = new YMLConfiguration(this, new File(getDataFolder() + "/config.yml"), "config.yml", false, true);

        // load locale file
        reloadLocale(GSMLocale.file);

        // load types
        types.clear();
        for (String id : configuration.getKeysForSection("types", false)) {
            ConfigurationSection section = getConfig().getConfigurationSection("types." + id);

            if (section == null) {
                continue;
            }

            double cost = section.getDouble("cost", 100D);
            String spinSound = section.getString("spin_sound", "ui.toast.in");
            String animationSound = section.getString("animation_sound", "block.wooden_button.click_on");
            String winSound = section.getString("win_sound", "entity.experience_orb.pickup");
            String loseSound = section.getString("lose_sound", "block.anvil.break");

            List<MachineItem> prizes = GSlotMachine.inst().getConfiguration()
                    .getKeysForSection("types." + id + ".prizes", true)
                    .stream()
                    .map(priceId -> {
                        String materialName = section.getString("prizes." + priceId + ".type");
                        Material material = MaterialUtils.fromString(materialName);

                        // Invalid material
                        if (material == null) {
                            getLogger().warning("Found invalid material '" + materialName + "'. Using DIRT.");
                            material = Material.DIRT;
                        }

                        Integer amount = section.getInt("prizes." + priceId + ".amount");
                        Double chance = section.getDouble("prizes." + priceId + ".chance");
                        Boolean giveItem = section.getBoolean("prizes." + priceId + ".give_item");
                        List<String> commands = section.getStringList("prizes." + priceId + ".commands");

                        ItemStack itemStack = new ItemStack(material, amount);

                        // Item is player head
                        if (section.contains("prizes." + priceId + ".player_uuid")) {
                            UUID ownerUuid = UUID.fromString(section.getString("prizes." + priceId + ".player_uuid"));
                            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(ownerUuid));
                            itemStack.setItemMeta(skullMeta);
                        }

                        // Item has custom name
						if (section.contains("prizes." + priceId + ".item_name")) {
							String itemName = ChatColor.translateAlternateColorCodes('&',
									section.getString("prizes." + priceId + ".item_name"));
							ItemMeta itemMeta = itemStack.getItemMeta();
							itemMeta.setDisplayName(itemName);
							itemStack.setItemMeta(itemMeta);
						}

						// Item has custom lore
						if (section.contains("prizes." + priceId + ".item_lore")) {
							List<String> itemLore = section.getStringList("prizes." + priceId + ".item_lore")
									.stream()
									.map(line -> ChatColor.translateAlternateColorCodes('&', line))
									.collect(Collectors.toList());
							ItemMeta itemMeta = itemStack.getItemMeta();
							itemMeta.setLore(itemLore);
							itemStack.setItemMeta(itemMeta);
						}

                        return new MachineItem(itemStack, amount, chance, giveItem, commands);
                    })
                    .collect(Collectors.toList());

            types.put(id, new MachineType(id, cost, spinSound, animationSound, winSound, loseSound, prizes));
        }

        // data manager
        if (dataManager == null) {
            BackEnd backend = getConfiguration().getEnumValue("data.backend", BackEnd.class, BackEnd.JSON);
            if (backend == null) {
                backend = BackEnd.JSON;
            }
            this.dataManager = new PSMDataManager(backend);
            dataManager.enable();
        } else {
            dataManager.synchronize();
        }

        // success
        return true;
    }

    // ----------------------------------------------------------------------
    // Enable
    // ----------------------------------------------------------------------

    @Override
    protected boolean enable() {
        // call reload
        innerReload();

        // events
        Bukkit.getPluginManager().registerEvents(this, this);

        // commands
        CommandRoot root = new CommandRoot(this, Utils.asList("machine"), null, null, false);
        registerCommand(root, GSMPerm.GSLOTMACHINE_ADMIN);
        root.addChild(new CommandCreate());
        root.addChild(new CommandSetbutton());
        root.addChild(new CommandSetcase());

        // success
        return true;
    }

    // ----------------------------------------------------------------------
    // Disable
    // ----------------------------------------------------------------------

    @Override
    protected void disable() {
    }

    // ----------------------------------------------------------------------
    // Listeners
    // ----------------------------------------------------------------------

    private List<Integer> events = new ArrayList<Integer>();

    @EventHandler
    public void run(PlayerInteractEvent event) {
        if (events.contains(event.hashCode())) return;
        events.add(event.hashCode());
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().toString().contains("BUTTON")) {
            Machine machine = getData().getMachines().getElementByButton(event.getClickedBlock().getLocation());
            if (machine != null && machine.getRunning() == null) {
                Player player = event.getPlayer();
                MachineType type = getMachineType(machine.getType());
                if (type == null) {
                    Messenger.send(player, Level.SEVERE_INFO, "GSlotMachine", "Machine type " + machine.getType() + " doesn't exists in the configuration.");
                    return;
                }
                if (machine.getCase(1) == null || machine.getCase(2) == null || machine.getCase(3) == null) {
                    Messenger.send(player, Level.SEVERE_INFO, "GSlotMachine", "Machine " + machine.getId() + " isn't correctly defined (need 3 cases).");
                    return;
                }
                if (type.getPrizes().size() < 5) {
                    Messenger.send(player, Level.SEVERE_INFO, "GSlotMachine", "Machine " + machine.getId() + " isn't correctly defined (need at least 5 items).");
                    return;
                }
                double balance = GCore.inst().getEconomyHandler().get(player);
                if (balance < type.getCost()) {
                    GLocale.MSG_GENERIC_NOMONEY.send(player, "{plugin}", getName(), "{balance}", Utils.round(balance), "{money}", Utils.round(type.getCost()));
                    return;
                }
                RunningMachine running = new RunningMachine(player, machine, type);
                machine.setRunningMachine(running);
                running.start();
            }
        }
    }

}
