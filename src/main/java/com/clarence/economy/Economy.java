package com.clarence.economy;

import com.clarence.ToolHelper.Configuration;
import com.clarence.ToolHelper.GithubInfo;
import com.clarence.ToolHelper.Util;
import com.clarence.economy.Command.Balance;
import com.clarence.economy.Command.Shop;
import com.technicjelle.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletionException;

public final class Economy extends JavaPlugin {
    private UpdateChecker updateChecker = null;
    public UpdateChecker getUpdateChecker() { return updateChecker; }
    @Override
    public void onEnable() {
        try {
            updateChecker = new UpdateChecker(GithubInfo.AUTHOR.getName(), GithubInfo.REPOSITORYNAME.getName(), GithubInfo.CURRENTVERSION.getName());
            updateChecker.checkAsync();
            updateChecker.logUpdateMessage(getLogger());
        } catch (CompletionException e) {
            System.out.println(Util.setMessage("COULD NOT FETCH THE LATEST UPDATE", false, false));
        }

        new Configuration();

        Bukkit.getPluginManager().registerEvents(new listener(), this);
        getCommand("balance").setExecutor(new Balance());
        getCommand("shop").setExecutor(new Shop());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
