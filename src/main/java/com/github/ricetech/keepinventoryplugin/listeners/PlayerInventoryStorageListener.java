package com.github.ricetech.keepinventoryplugin.listeners;

import com.github.ricetech.keepinventoryplugin.commands.RestoreInventoryCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerInventoryStorageListener implements Listener {
    @EventHandler
    public void storeInventoryOnDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        List<ItemStack> items = new ArrayList<>();
        PlayerInventory inventory = p.getInventory();
        Collections.addAll(items, inventory.getContents());
        Collections.addAll(items, inventory.getArmorContents());
        //noinspection DataFlowIssue
        RestoreInventoryCommand.putInventory(p.getName(), (ItemStack[]) items.toArray());
    }
}
