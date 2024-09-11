package com.clarence.economy.Command;

import com.clarence.ToolHelper.Configuration;
import com.clarence.ToolHelper.Messages;
import com.clarence.ToolHelper.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Shop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.setMessage(Messages.CONSOLE.getMessage(), false, false));
            return true;
        }

        if (args.length == 0) {
            List<String> itemKeys = new ArrayList<>(Configuration.getItemsConfiguration().getKeys(false));

            if (itemKeys.isEmpty()) {
                player.sendMessage(Util.setMessage(Messages.SHOP_ITEM_EMPTY.getMessage(), true, true));
                return true;
            }

            String message = Messages.SHOP_ITEMS.getMessage().replace("%Shop_items%", itemKeys.toString());

            player.sendMessage(Util.setMessage(message, true, true));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "check":
                itemCheck(player, args);
                break;
            case "add":
                addItemsToItemsYml(player, args);
                break;
            case "buy":
                handleBuy(player, args);
                break;
            case "sell":
                handleSell(player, args);
                break;
            case "help":
                player.sendMessage(Util.setMessage(Messages.SHOP_HELP.getMessage(), true, true));
                break;
        }
        return false;
    }

    private void itemCheck(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(Util.setMessage(Messages.SHOP_ITEM_CHECK_NOT_SPECIFY.getMessage(), true, true));
            return;
        }

        String arg = args[1];

        if (Configuration.getItemsConfiguration().getConfigurationSection(arg) == null) {
            player.sendMessage(Util.setMessage(Messages.SHOP_ITEM_CHECK_NOT_FOUND.getMessage(), true, true));
            return;
        }

        ConfigurationSection configurationSection = Configuration.getItemsConfiguration().getConfigurationSection(arg);

        String itemMaterial = configurationSection.getString("Name");
        int itemBuyPrice = configurationSection.getInt("Price");
        int itemSellPrice = configurationSection.getInt("Sell");


        String message = Messages.SHOP_ITEM_CHECK.getMessage().replace("%Shop_item_material%", itemMaterial).replace("%Shop_item_price%", String.valueOf(itemBuyPrice)).replace("%Shop_item_sell_price%", String.valueOf(itemSellPrice));

        player.sendMessage(Util.setMessage(message, true, true));


    }

    private void addItemsToItemsYml(Player player, String[] args) {
        try {
                player.getInventory().getItemInMainHand().getItemMeta().getItemName();
        } catch (NullPointerException e) {
            player.sendMessage(Util.setMessage(Messages.ADD_ITEM_NO_ITEM_FOUND_IN_HAND.getMessage(), true, true));
            return;
        }

        String itemStack_Type_name = player.getInventory().getItemInMainHand().getType().name();

        if (args.length == 1) {
            player.sendMessage(Util.setMessage(Messages.CONFIGURATION_ITEM_ADD_BUY_PRICE.getMessage(), true, true));
            return;
        }
        try {
            Integer.parseInt(args[1]);
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
            String message = Messages.ADD_ITEM_NO_ITEM_PRICE_FOUND.getMessage().replace("%item%", itemStack_Type_name);
            player.sendMessage(Util.setMessage(message, true, true));
            return;
        }

        if (args.length == 2) {
            player.sendMessage(Util.setMessage(Messages.CONFIGURATION_ITEM_ADD_SELL_PRICE.getMessage(), true, true));
            return;
        }

        try {
            Integer.parseInt(args[2]);
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage(Messages.CONFIGURATION_ITEM_ADD_SELL_PRICE.getMessage(), true, true));
        }

        int itemBuyPrice = Integer.parseInt(args[1]);
        int itemSellPrice = Integer.parseInt(args[2]);

        Configuration.GenerateNewItemData(player, itemStack_Type_name, itemBuyPrice, itemSellPrice);
    }

    private void handleBuy(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(Util.setMessage(Messages.BUY_ITEM_NO_ITEM_FOUND.getMessage(), true, true));
            return;
        }
        String item = args[1];

        try {
            ConfigurationSection configurationSection = Configuration.getItemsConfiguration().getConfigurationSection(item);
            Material.valueOf(configurationSection.getString("Name"));
        } catch (NullPointerException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage(Messages.BUY_ITEM_NO_ITEM_FOUND_IN_CONFIGURATION.getMessage(), true, true));
            return;
        }

        if (args.length == 2) {
            player.sendMessage(Util.setMessage(Messages.BUY_ITEM_AMOUNT.getMessage(), true, true));
            return;
        }

        try {
            int quality = Integer.parseInt(args[2]);
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
            return;
        }

        ConfigurationSection ItemsConfigurationSection = Configuration.getItemsConfiguration().getConfigurationSection(item);
        ConfigurationSection balanceConfigurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(player.getUniqueId().toString());

        int itemQuantity = Integer.parseInt(args[2]);
        int price = ItemsConfigurationSection.getInt("Price");

        int quantityPrice = price * itemQuantity;

        if (itemQuantity > 0) {
            if (balanceConfigurationSection.getInt("Money") < quantityPrice && !player.isOp()) {
                String message = Messages.BUY_ITEM_NOT_ENOUGH_FUNDS.getMessage().replace("%Shop_item_material%", item);
                player.sendMessage(Util.setMessage(message, true, true));
                return;
            }
            ItemStack itemStack = Util.getItemStack(Material.matchMaterial(item), itemQuantity);

            if (player.isOp()) {
                player.getInventory().addItem(itemStack);
                player.sendMessage(Util.setMessage("You spawned in x" + itemQuantity + " " + item, true, true));
                return;
            }

            balanceConfigurationSection.set("Money", balanceConfigurationSection.getInt("Money", 0) - quantityPrice);

            player.getInventory().addItem(itemStack);
            String message = Messages.BUY_ITEM_SUCCESS.getMessage().replace("%item_amount%", String.valueOf(quantityPrice)).replace("%Shop_item_material%", item).replace("%Shop_item_price%", String.valueOf(quantityPrice));
            player.sendMessage(Util.setMessage(message, true, true));

            Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration(), player);
        } else {
            player.sendMessage(Util.setMessage("You cannot buy less then x0 " + item, true, true));
        }
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

        int itemQuantity = Integer.parseInt(args[1]);
        int finalAmount = itemInMainHandItemStack.getAmount() - itemQuantity;

        if (itemInMainHandItemStack.getAmount() < itemQuantity) {
            player.sendMessage(Util.setMessage("Insufficient item amount", true, true));
            return;
        }

        itemInMainHandItemStack.setAmount(finalAmount);

        player.sendMessage(Util.setMessage("You have sold x" + itemQuantity + " " +  itemName, true, true));
        ConfigurationSection configurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(String.valueOf(player.getUniqueId()));
        int money = getMoney(configurationSection, player, itemQuantity);
        configurationSection.set("Money", money);
        Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration(), player);
    }
    private int getMoney(ConfigurationSection configurationSection, Player player, int value) {
        int money = configurationSection.getInt("Money", 0) + value;
        player.sendMessage(Util.setMessage("You have been given $" + value, true, true));
        return money;
    }
}
