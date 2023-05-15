package com.github.ricetech.keepinventoryplugin.commands;

import com.github.ricetech.keepinventoryplugin.KeepInventoryPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RestoreInventoryCommand implements CommandExecutor {
    private final static Map<String, ItemStack[]> inventoryContents = new HashMap<>();

    public static ItemStack[] getInventoryContents(String entry) {
        return inventoryContents.getOrDefault(entry, null);
    }

    public static void putInventory(String entry, ItemStack[] inventoryContents) {
        RestoreInventoryCommand.inventoryContents.put(entry, inventoryContents);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        Player p = Bukkit.getPlayerExact(args[0]);

        if (p == null) {
            KeepInventoryPlugin.sendErrorMsg(sender, "Target player does not exist or is offline.");
            return true;
        }

        ItemStack[] inventoryContents = RestoreInventoryCommand.inventoryContents.getOrDefault(p.getName(), null);

        if (inventoryContents == null) {
            KeepInventoryPlugin.sendErrorMsg(sender, "Target player does not have a stored inventory.");
            return true;
        }

        Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER, Component.text("Your previous inventory"));
        inventory.setContents(inventoryContents);

        p.openInventory(inventory);

        return true;
    }
}
