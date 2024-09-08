package com.clarence.ToolHelper;

import com.clarence.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private static final Economy EconomyPlugin = Util.getEconomyPlugin();

    private static File balanceFile = null;
    private static FileConfiguration balanceConfiguration = null;
    public static File getBalanceFile() {return balanceFile;}
    public static FileConfiguration getBalanceConfiguration() {return balanceConfiguration;}


    public Configuration() {
        balanceFile = getFile("Balance");
        balanceConfiguration = getConfiguration(balanceFile);
        saveConfiguration(balanceFile, balanceConfiguration);
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
    public static void saveConfiguration(File file, FileConfiguration fileConfiguration) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void reloadFiles(Player player) {
        if (!EconomyPlugin.getDataFolder().exists()) {
            System.out.println(Util.setMessage("COULD NOT FIND FOLDER DIRECTORY", false, false));
            return;
        }

        if (balanceFile == null) {
            System.out.println(Util.setMessage("COULD NOT FIND FILE", false, false));
            return;
        }

        if (!balanceFile.exists()) {
            System.out.println(Util.setMessage("COULD NOT FIND FILE", false, false));
            return;
        }

        balanceConfiguration = YamlConfiguration.loadConfiguration(balanceFile);
        System.out.println(Util.setMessage("Files have been reloaded", false, false));
        player.sendMessage(Util.setMessage("Files have been reloaded", true, true));
    }

    private String getFileName(String name) { return name + ".yml"; }
    public static void GenerateNewData(Player player) {
        ConfigurationSection configurationSection = Configuration.getBalanceConfiguration().createSection(String.valueOf(player.getUniqueId()));
        configurationSection.set("Name", player.getDisplayName());
        configurationSection.set("Money", 0);
        Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration());
        player.sendMessage(Util.setMessage("Generated a new data for " + player.getDisplayName(), true, true));
    }
}
