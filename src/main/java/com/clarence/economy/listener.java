package com.clarence.economy;

import com.clarence.ToolHelper.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class listener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Configuration.getBalanceConfiguration().get(player.getUniqueId().toString()) == null) {
            Configuration.GenerateNewData(player);
        }
    }
}
