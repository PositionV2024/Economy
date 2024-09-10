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
            sender.sendMessage(Util.setMessage("CAN'T DO THIS THROUGH CONSOLE", false, false));
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
            case "buy":
                handleBuy(player, args);
                break;
            case "sell":
                handleSell(player, args);
                break;
            case "check":
                itemCheck(player, args);
                break;
            case "add":
                addItemsToConfig(player, args);
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

        String itemMaterial = configurationSection.getString("Material");
        int itemPrice = configurationSection.getInt("Price");

        String message = Messages.SHOP_ITEM_CHECK.getMessage().replace("%Shop_item_material%", itemMaterial).replace("%Shop_item_price%", String.valueOf(itemPrice));

        player.sendMessage(Util.setMessage(message, true, true));


    }

    private void addItemsToConfig(Player player, String[] args) {
        String itemPath = Configuration.getItemsFile().getPath();

        if (args.length == 1) {
            player.sendMessage(Util.setMessage("Please specific a material to add to " + itemPath, true, true));
            return;
        }
        String arg = args[1];

        if (Configuration.getItemsConfiguration().getConfigurationSection(arg) != null) {
            player.sendMessage(Util.setMessage("The item already exist", true, true));
            return;
        }

        if (args.length == 2) {
            player.sendMessage(Util.setMessage("Add a price to the item", true, true));
            return;
        }
        try {
            int itemPrice = Integer.valueOf(args[2]);
        }catch (NumberFormatException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("Invalid price", true, true));
        }
        int itemPrice = Integer.valueOf(args[2]);

        Material material = null;
        try {
            material = Material.valueOf(arg);
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("Please add a material to " + itemPath, true, true));
            return;
        }
        Configuration.GenerateNewItemData(player, material, itemPrice);
        player.sendMessage(Util.setMessage("Added item &a" + arg + "&b with the price of &a$" + itemPrice + "/each &bin " + itemPath, true, true));
    }

    private void handleBuy(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(Util.setMessage("Please specify the item you want to buy", true, true));
            return;
        }
        String arg = args[1];
        try {
            ConfigurationSection configurationSection = Configuration.getItemsConfiguration().getConfigurationSection(arg);

            Material.valueOf(String.valueOf(configurationSection.getString("Material")));
        } catch (NullPointerException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("MATERIAL CANNOT BE FOUND", true, true));
            return;
        }

        if (args.length == 2) {
            player.sendMessage(Util.setMessage("Please specify the quality you want to buy", true, true));
            return;
        }

        try {
            int quality = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage("INVALID QUAlITY", true, true));
            return;
        }

        ConfigurationSection ItemsConfigurationSection = Configuration.getItemsConfiguration().getConfigurationSection(arg);
        ConfigurationSection balanceConfigurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(player.getUniqueId().toString());

        int itemQuantity = Integer.parseInt(args[2]);
        int price = ItemsConfigurationSection.getInt("Price");

        int quantityPrice = price * itemQuantity;

        if (itemQuantity != 0) {
            if (balanceConfigurationSection.getInt("Money") < quantityPrice) {
                player.sendMessage(Util.setMessage("You have insufficient funds to buy " + arg.toLowerCase(), true, true));
                return;
            }
            ItemStack itemStack = Util.getItemStack(Material.matchMaterial(args[1]), itemQuantity, arg, "");
            player.getInventory().addItem(itemStack);
            int Money = balanceConfigurationSection.getInt("Money", 0) - quantityPrice;
            balanceConfigurationSection.set("Money", Money);
            player.sendMessage(Util.setMessage("You bought x" + itemQuantity + " " + arg + " for $" + quantityPrice, true, true));
        } else {
            player.sendMessage(Util.setMessage("You cannot buy 0 " + arg, true, true));
        }

        Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration(), player);
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
        player.sendMessage(Util.setMessage(itemInMainHandItemStack.getType().toString(), true, true));

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
