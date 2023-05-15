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

import java.util.*;

public class RestoreInventoryCommand implements CommandExecutor {
    public static final int MAX_INVENTORIES_STORED = 5; // (per player)
    private static final Map<String, LinkedList<ItemStack[]>> inventoryContents = new HashMap<>();

    public static void putInventory(String playerName, ItemStack[] inventoryContents) {
        LinkedList<ItemStack[]> existingDeque = RestoreInventoryCommand.inventoryContents.putIfAbsent(playerName, new LinkedList<>(Collections.singleton(inventoryContents)));
         if (existingDeque != null) {
             existingDeque.addFirst(inventoryContents);
             if (existingDeque.size() > MAX_INVENTORIES_STORED) {
                 existingDeque.removeLast();
             }
         }
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1 || args.length > 3) {
            return false;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        Player commandSender;

        if (args.length == 3 && Objects.equals(args[2], "true") && !(sender instanceof Player)) {
            KeepInventoryPlugin.sendErrorMsg(sender, "You cannot open an inventory from the command line. " +
                    "Either run this command in-game, or allow the player to see their inventory by omitting the second option.");
            return true;
        } else {
             commandSender = (Player) sender;
        }

        if (targetPlayer == null && (args.length == 1 || args.length == 2 || !Objects.equals(args[2], "true"))) {
            KeepInventoryPlugin.sendErrorMsg(sender, "Target player does not exist or is offline.");
            return true;
        }

        // If the second arg is not provided, we assume they want the most recent death
        int invIndex;
        try {
            invIndex = args.length == 1 ? 0 : Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException e) {
            return false;
        }

        // Get inventory
        LinkedList<ItemStack[]> inventories = RestoreInventoryCommand.inventoryContents.getOrDefault(args[0], null);
        if (inventories == null) {
            KeepInventoryPlugin.sendErrorMsg(sender, "Target player does not have a stored inventory. Or they don't exist.");
            return true;
        }

        if (invIndex < 0 || invIndex >= inventories.size()) {
            KeepInventoryPlugin.sendErrorMsg(sender, "Target player only has "+ inventories.size() + " stored inventories." +
                    "Provide a number between 1 and " + inventories.size() + " for the second argument.");
            return true;
        }
        ItemStack[] inventoryContents = inventories.get(invIndex);

        Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER, Component.text("Your previous inventory"));
        inventory.setContents(inventoryContents);

        if (args.length == 1 || args.length == 2 || !Objects.equals(args[2], "true")) {
            assert targetPlayer != null; // Already checked above
            targetPlayer.openInventory(inventory);
        } else {
            commandSender.openInventory(inventory);
        }

        return true;
    }
}
