package com.clarence.ToolHelper;

import com.clarence.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private static final Economy EconomyPlugin = Util.getEconomyPlugin();

    private static File balanceFile = null;
    private static FileConfiguration balanceConfiguration = null;
    private static File itemsFile = null;
    private static FileConfiguration itemsConfiguration = null;
    private static List<File> files = new ArrayList<>();

    public static File getBalanceFile() {return balanceFile;}
    public static FileConfiguration getBalanceConfiguration() {return balanceConfiguration;}
    public static File getItemsFile() {return itemsFile;}
    public static FileConfiguration getItemsConfiguration() {return itemsConfiguration;}


    public Configuration() {
        balanceFile = getFile("Balance");
        balanceConfiguration = getConfiguration(balanceFile);

        itemsFile = getFile("Items");
        itemsConfiguration = getConfiguration(itemsFile);

        files.add(balanceFile); files.add(itemsFile);

        saveConfiguration(balanceFile, balanceConfiguration, null);
        saveConfiguration(itemsFile, itemsConfiguration, null);
    }

    private File getFile(String name) {
        File file = new File(EconomyPlugin.getDataFolder(), getFileName(name));

        if (!EconomyPlugin.getDataFolder().exists()) {
            System.out.println(Util.setMessage("COULD NOT FIND FOLDER DIRECTORY. CREATING A NEW FOLDER DIRECTORY", false, false));
            EconomyPlugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(Util.setMessage("COULD NOT CREATE FILE", false, false));
            }
        }
        return file;
    }

    private FileConfiguration getConfiguration(File file) {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        return fileConfiguration;
    }
    public static void saveConfiguration(File file, FileConfiguration fileConfiguration, Player player) {
        if (!EconomyPlugin.getDataFolder().exists()) {
            System.out.println(Util.setMessage("COULD NOT FIND FOLDER DIRECTORY", false, false));
            return;
        }

        if (file == null) {
            System.out.println(Util.setMessage("COULD NOT FIND FILE", false, false));
            return;
        }

        if (!file.exists()) {
            System.out.println(Util.setMessage("COULD NOT FIND FILE", false, false));
            return;
        }

        try {
            fileConfiguration.save(file);
            System.out.println(Util.setMessage("Saved " + file, false, false));

            if (player != null) {
                player.sendMessage(Util.setMessage("Saved " + file, false, false));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void reloadFiles(Player player) {
        if (!EconomyPlugin.getDataFolder().exists()) {
            System.out.println(Util.setMessage("COULD NOT FIND FOLDER DIRECTORY", false, false));
            return;
        }

        for (File file : files) {
            if (file == null) {
                System.out.println(Util.setMessage("COULD NOT FIND FILE", false, false));
                return;
            }

            if (!file.exists()) {
                System.out.println(Util.setMessage("COULD NOT FIND FILE", false, false));
                return;
            }
        }

        balanceConfiguration = YamlConfiguration.loadConfiguration(balanceFile);
        itemsConfiguration = YamlConfiguration.loadConfiguration(itemsFile);

        System.out.println(Util.setMessage("Files have been reloaded", false, false));
        player.sendMessage(Util.setMessage("Files have been reloaded", true, true));
    }

    private String getFileName(String name) { return name + ".yml"; }
    public static void GenerateNewBalanceData(Player player) {
        ConfigurationSection configurationSection = Configuration.getBalanceConfiguration().createSection(String.valueOf(player.getUniqueId()));
        configurationSection.set("Name", player.getDisplayName());
        configurationSection.set("Money", 0);
        Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration(), player);
        player.sendMessage(Util.setMessage("Generated a new data for " + player.getDisplayName(), true, true));
    }
    public static void GenerateNewItemData(Player player, String material, int itemBuyPrice, int itemSellPrice) {
        if (getItemsConfiguration().getConfigurationSection(material) != null) {
            String message = Messages.CONFIGURATION_ITEM_HAS_ALREADY_BEEN_ADDED.getMessage().replace("%Shop_item_material%", material);
            player.sendMessage(Util.setMessage(message, true, true));
            return;
        }
        ConfigurationSection configurationSection = Configuration.getItemsConfiguration().createSection(material);
        configurationSection.set("Name", material);
        configurationSection.set("Price", itemBuyPrice);
        configurationSection.set("Sell", itemSellPrice);

        String message = Messages.CONFIGURATION_ITEM_ADDED.getMessage().replace("%Shop_item_material%", material).replace("%Shop_item_price%", String.valueOf(itemBuyPrice));

        player.sendMessage(Util.setMessage(message, true, true));
        Configuration.saveConfiguration(Configuration.getItemsFile(), Configuration.getItemsConfiguration(), player);
    }
}
