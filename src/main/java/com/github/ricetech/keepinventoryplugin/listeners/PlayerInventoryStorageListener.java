package com.github.ricetech.keepinventoryplugin.listeners;

import com.github.ricetech.keepinventoryplugin.commands.RestoreInventoryCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryStorageListener implements Listener {
    @EventHandler
    public void storeInventoryOnDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        PlayerInventory inventory = p.getInventory();
        int numInvItems = inventory.getContents().length;
        int numArmor = inventory.getArmorContents().length;
        ItemStack[] items = new ItemStack[45];
        System.arraycopy(inventory.getContents(), 0, items, 0, numInvItems);
        System.arraycopy(inventory.getArmorContents(), 0, items, numInvItems, numArmor);
        for (int i = numInvItems + numArmor; i < 45; i++) {
            items[i] = new ItemStack(Material.AIR);
        }

        RestoreInventoryCommand.putInventory(p.getName(), items);
    }
}
