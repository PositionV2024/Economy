package com.clarence.economy.Command;

import com.clarence.ToolHelper.Configuration;
import com.clarence.ToolHelper.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Shop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.setMessage("CAN'T DO THIS THROUGH CONSOLE", false, false));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Util.setMessage("USAGE: BUY | SELL", true, true));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "buy":
                handleBuy(player, args);
                break;
            case "sell":
                handleSell(player, args);
                break;
        }
        return false;
    }
    private void handleBuy(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(Util.setMessage("Please specify the item you want to buy", true, true));
            return;
        }
        try {
            Material.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("MATERIAL CANNOT BE FOUND", true, true));
            return;
        }

        if (args.length == 2) {
            player.sendMessage(Util.setMessage("Please specify the quality you want to buy", true, true));
            return;
        }

        try {
            Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("INVALID QUAlITY", true, true));
            return;
        }

        ItemStack itemStack = Util.getItemStack(Material.matchMaterial(args[1]), Integer.parseInt(args[2]), args[1], "");
        player.getInventory().addItem(itemStack);
        player.sendMessage(Util.setMessage("You bought x" + args[2] + " " + args[1].toLowerCase(), true, true));
    }
    private void handleSell(Player player, String[] args) {
        try {
            player.getInventory().getItemInMainHand().getItemMeta().getItemName();
        } catch (NullPointerException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("Please place the item you want to sell in your hand", true, true));
            return;
        }
        ItemStack itemInMainHandItemStack = player.getInventory().getItemInMainHand();

        String itemName = itemInMainHandItemStack.getItemMeta().getItemName();

        if (args.length == 1) {
            player.sendMessage(Util.setMessage("Please specify the quality you want to sell", true, true));
            return;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("INVALID QUAlITY", true, true));
            return;
        }

        int itemRemoveAmount = Integer.parseInt(args[1]);
        int finalAmount = itemInMainHandItemStack.getAmount() - itemRemoveAmount;

        if (itemInMainHandItemStack.getAmount() < itemRemoveAmount) {
            player.sendMessage(Util.setMessage("Insufficient item amount", true, true));
            return;
        }

        itemInMainHandItemStack.setAmount(finalAmount);

        player.sendMessage(Util.setMessage("You have sold x" + itemRemoveAmount + " " +  itemName, true, true));
        ConfigurationSection configurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(String.valueOf(player.getUniqueId()));
        int money = getMoney(configurationSection, player, itemRemoveAmount);
        configurationSection.set("Money", money);
        Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration());
    }
    private int getMoney(ConfigurationSection configurationSection, Player player, int value) {
        int money = configurationSection.getInt("Money", 0) + value;
        player.sendMessage(Util.setMessage("You have been given $" + value, true, true));
        return money;
    }
}
