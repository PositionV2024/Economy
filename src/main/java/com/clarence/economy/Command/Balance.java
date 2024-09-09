package com.clarence.economy.Command;

import com.clarence.ToolHelper.*;
import com.technicjelle.UpdateChecker;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
            case "give":
                giveBalance(player, args);
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

    private void giveBalance(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(Util.setMessage(Messages.BALANCE_GIVE_PLAYER_DOESNT_EXIST.getMessage(), true, true));
            return;
        }
        String arg = args[1];

        OfflinePlayer getOfflinePlayerByUUID = player.getServer().getOfflinePlayer(arg);
        UUID getOfflinePlayerByUUIDUniqueId = getOfflinePlayerByUUID.getUniqueId();

        if (!getOfflinePlayerByUUID.hasPlayedBefore()) {
            player.sendMessage(Util.setMessage(Messages.BALANCE_GIVE_TARGET_DOSENT_EXIST.getMessage(), true, true));
            return;
        }
        if (Configuration.getBalanceConfiguration().getConfigurationSection(getOfflinePlayerByUUIDUniqueId.toString()) == null) {
            String message = Messages.BALANCE_GIVE_TARGET_IS_NOT_IN_BALANCE_FILE.getMessage().replace("%Balance_file_path%", Configuration.getBalanceFile().getPath());
            player.sendMessage(Util.setMessage(message, true, true));
            return;
        }
        if (args.length == 2) {
            player.sendMessage(Util.setMessage(Messages.BALANCE_GIVE_AMOUNT_DOESNT_EXIST.getMessage(), true, true));
            return;
        }

        try {
            Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            e.getStackTrace();
            player.sendMessage(Util.setMessage(Messages.BALANCE_GIVE_AMOUNT_INVALID.getMessage(), true, true));
        }
        int amount = Integer.parseInt(args[2]);

        String message = Messages.BALANCE_GIVE_SUCCESS.getMessage().replace("%target_display_name%", getOfflinePlayerByUUID.getName()).replace("%amount_given%", String.valueOf(amount));

        ConfigurationSection targetConfigurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(getOfflinePlayerByUUIDUniqueId.toString());
        ConfigurationSection PlayerConfigurationSection = Configuration.getBalanceConfiguration().getConfigurationSection(player.getUniqueId().toString());

        if (PlayerConfigurationSection.getInt("Money") < amount) {
            player.sendMessage(Util.setMessage(Messages.BALANCE_GIVE_NOT_ENOUGH_FUNDS.getMessage(), true, true));
            return;
        }
        PlayerConfigurationSection.set("Money", PlayerConfigurationSection.getInt("Money", 0) - amount);
        targetConfigurationSection.set("Money", targetConfigurationSection.getInt("Money", 0) + amount);

        player.sendMessage(Util.setMessage(message, true, true));
        Configuration.saveConfiguration(Configuration.getBalanceFile(), Configuration.getBalanceConfiguration(), player);
    }

    private void FetchedData(Player player){
        int Money = Configuration.getBalanceConfiguration().getConfigurationSection(player.getUniqueId().toString()).getInt("Money");
        uuid.getUUID().put(player.getUniqueId(), Money);
        String balance = Messages.BALANCE_FETCHED_DATA.getMessage().replace("%hashmap_player%", uuid.getUUID().get(player.getUniqueId()).toString());

        player.sendMessage(Util.setMessage(balance, true, true));
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
