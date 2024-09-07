package com.clarence.economy.Command;

import com.clarence.ToolHelper.Util;
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
            player.sendMessage(Util.setMessage("USAGE: /BALANCE", true, true));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "version":
                try {
                    if (!Util.getEconomyPlugin().getUpdateChecker().isUpdateAvailable()) {
                        player.sendMessage(Util.setMessage("Your current version is " + Util.getEconomyPlugin().getDescription().getVersion(), true, true));
                        return true;
                    }
                    player.sendMessage(Util.setMessage("Your on an outdated version. Please download the newest version here: " + Util.getEconomyPlugin().getUpdateChecker().getUpdateUrl(), true, true));
                } catch (CompletionException e) {
                   player.sendMessage(Util.setMessage("COULD NOT FETCH THE LATEST UPDATE", true, true));
                }
                break;
        }

        return false;
    }
}
