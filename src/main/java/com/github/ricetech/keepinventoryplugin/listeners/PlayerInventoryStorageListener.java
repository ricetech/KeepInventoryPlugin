package com.github.ricetech.keepinventoryplugin.listeners;

import com.github.ricetech.keepinventoryplugin.commands.RestoreInventoryCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerInventoryStorageListener implements Listener {
    @EventHandler
    public void storeInventoryOnDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        RestoreInventoryCommand.putInventory(p.getName(), p.getInventory().getContents());
    }
}
