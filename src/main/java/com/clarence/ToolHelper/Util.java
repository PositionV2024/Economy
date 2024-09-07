package com.clarence.ToolHelper;

import com.clarence.economy.Economy;
import org.bukkit.ChatColor;

public class Util {
    private static Economy economy = Economy.getPlugin(Economy.class);
    public static Economy getEconomyPlugin() { return economy; }
    public static String setMessage(String message, boolean isPlayerMessage, boolean isColored) {
        if (isPlayerMessage) {
            if (isColored) {
                return ChatColor.translateAlternateColorCodes('&', "&b&l[" + economy.getName() + "] » " + message + ".");
            } else {
                return "[" + economy.getName() + "] » " + message + ".";
            }
        } else {
            return "[" + economy.getName() + "] " + message + ".";
        }
    }
}
