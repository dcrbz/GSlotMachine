package com.guillaumevdn.gslotmachine.machine;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MachineItem {

    private ItemStack itemStack;
    private Integer amount;
    private Double chance;
    private Boolean giveItem;
    private List<String> commands;

    public MachineItem() {
    }

    public MachineItem(ItemStack itemStack, Integer amount, Double chance, Boolean giveItem, List<String> commands) {
        this.itemStack = itemStack;
        this.amount = amount;
        this.chance = chance;
        this.giveItem = giveItem;
        this.commands = commands;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getChance() {
        return chance;
    }

    public void setChance(Double chance) {
        this.chance = chance;
    }

    public Boolean getGiveItem() {
        return giveItem;
    }

    public void setGiveItem(Boolean giveItem) {
        this.giveItem = giveItem;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void give(Player player) {
        player.getInventory().addItem(getItemStack());
    }

}
