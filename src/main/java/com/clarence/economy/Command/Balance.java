package com.clarence.economy.Command;

import com.clarence.ToolHelper.*;
import com.technicjelle.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

public class Balance implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.setMessage(Messages.CONSOLE.getMessage(), false, false));
            return true;
        }
        if (args.length == 0) {
            if (Configuration.getBalanceConfiguration().get(player.getUniqueId().toString()) == null) {
                Configuration.GenerateNewBalanceData(player);
            } else {
                FetchedData(player);
            }
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "version":
                versionCheck(player);
                break;
            case "reload":
                Configuration.reloadFiles(player);
                break;
            case "clear":
                clearBalance(player, args);
                break;
            case "help":
                player.sendMessage(Util.setMessage(Messages.BALANCE_USAGE.getMessage(), true, true));
                break;
            default:
                player.sendMessage(Util.setMessage(Messages.BALANCE_HELP.getMessage(), true, true));
                break;
        }

        return false;
    }
    private void FetchedData(Player player){
        int Money = Configuration.getBalanceConfiguration().getConfigurationSection(player.getUniqueId().toString()).getInt("Money");
        uuid.getUUID().put(player.getUniqueId(), Money);

        player.sendMessage(Util.setMessage(Messages.BALANCE_FETCHED_DATA.getMessage() + Messages.getBalance(player), true, true));
    }
    private void versionCheck(Player player) {
        UpdateChecker updateChecker = Util.getEconomyPlugin().getUpdateChecker();
        try {
            if (!updateChecker.isUpdateAvailable()) {
                player.sendMessage(Util.setMessage(Colors.GRAY.getCode() +"Your current version is " + Colors.GREEN + updateChecker.getCurrentVersion(), true, true));
                return;
            }
            player.sendMessage(Util.setMessage(Colors.GRAY.getCode() + "Your current version is " + Colors.GREEN.getCode() + updateChecker.getCurrentVersion() + "\n" + Colors.GRAY.getCode() + "Please download the newest version " + Colors.GREEN + updateChecker.getLatestVersion() + Colors.GRAY.getCode() + "here: " + Colors.GREEN.getCode() + updateChecker.getUpdateUrl(), true, true));
        } catch (CompletionException e) {
            player.sendMessage(Util.setMessage("COULD NOT FETCH THE LATEST UPDATE", true, true));
        }
    }
    private void clearBalance(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(Util.setMessage(Colors.GRAY.getCode() + "USAGE: all | mine", true, true));
            return;
        }
        List<String> balanceList = new ArrayList<>(Configuration.getBalanceConfiguration().getKeys(false));

        switch (args[1].toLowerCase()) {
            case "all":
                for (int i = 0; i < balanceList.size(); i++) {
                    String name = balanceList.get(i);

                    ConfigurationSection configurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(name);
                    configurationSection.set("Money", 0);
                }
                Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration(), player);
                player.sendMessage(Util.setMessage(Colors.GRAY.getCode() + "Cleared all balances", true, true));
                break;
            case "mine":
                ConfigurationSection configurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(String.valueOf(player.getUniqueId()));
                configurationSection.set("Money", 0);
                Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration(), player);
                player.sendMessage(Util.setMessage(Colors.GRAY.getCode()+"Cleared your balance", true, true));
                break;
        }
    }
}
