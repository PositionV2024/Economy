package com.clarence.economy.Command;

import com.clarence.ToolHelper.Configuration;
import com.clarence.ToolHelper.Util;
import com.clarence.ToolHelper.uuid;
import com.technicjelle.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletionException;

public class Balance implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Util.setMessage("CAN'T DO THIS THROUGH CONSOLE", false, false));
            return true;
        }

        if (args.length == 0) {
            if (!uuid.getUUID().containsKey(player.getUniqueId())) {
                uuid.getUUID().put(player.getUniqueId(), 0);
            }

            player.sendMessage(Util.setMessage("Your balance is " + uuid.getUUID().get(player.getUniqueId()), true, true));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "version":
                versionCheck(player);
                break;
            case "reload":
                Configuration.reloadFiles(player);
                break;
            default:
                player.sendMessage(Util.setMessage("USAGE: balance | version | reload", true, true));
                break;
        }

        return false;
    }
    private void versionCheck(Player player) {
        UpdateChecker updateChecker = Util.getEconomyPlugin().getUpdateChecker();
        try {
            if (!updateChecker.isUpdateAvailable()) {
                player.sendMessage(Util.setMessage("Your current version is " + updateChecker.getCurrentVersion(), true, true));
                return;
            }
            player.sendMessage(Util.setMessage("Your current version is " + updateChecker.getCurrentVersion() + "\nPlease download the newest version " + updateChecker.getLatestVersion() + "here: " + updateChecker.getUpdateUrl(), true, true));
        } catch (CompletionException e) {
            player.sendMessage(Util.setMessage("COULD NOT FETCH THE LATEST UPDATE", true, true));
        }
    }
}
