package com.clarence.ToolHelper;

import com.clarence.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Util {
    private static Economy economy = Economy.getPlugin(Economy.class);
    public static Economy getEconomyPlugin() { return economy; }
    public static String setMessage(String message, boolean isPlayerMessage, boolean isColored) {
        if (isPlayerMessage) {
            if (isColored) {
                return ChatColor.translateAlternateColorCodes('&', "&b[" + economy.getName() + "] » " + message + ".");
            } else {
                return "[" + economy.getName() + "] » " + message + ".";
            }
        } else {
            return "[" + economy.getName() + "] " + message + ".";
        }
    }
    public static String setColor(String message) {return ChatColor.translateAlternateColorCodes('&', message); }
    public static ItemStack getItemStack(Material itemMaterial, int itemAmount) {
        ItemStack itemStack = new ItemStack(itemMaterial, itemAmount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
