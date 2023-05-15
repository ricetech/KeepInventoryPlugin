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
import java.util.Objects;

public class RestoreInventoryCommand implements CommandExecutor {
    private final static Map<String, ItemStack[]> inventoryContents = new HashMap<>();

    public static ItemStack[] getInventoryContents(String entry) {
        return inventoryContents.getOrDefault(entry, null);
    }

    public static void putInventory(String entry, ItemStack[] inventoryContents) {
        RestoreInventoryCommand.inventoryContents.put(entry, inventoryContents);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1 && args.length != 2) {
            return false;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        Player commandSender;

        if (args.length == 2 && Objects.equals(args[1], "true") && !(sender instanceof Player)) {
            KeepInventoryPlugin.sendErrorMsg(sender, "You cannot open an inventory from the command line. " +
                    "Either run this command in-game, or allow the player to see their inventory by omitting the second option.");
            return true;
        } else {
             commandSender = (Player) sender;
        }

        if (targetPlayer == null && (args.length == 1 || !Objects.equals(args[1], "true"))) {
            KeepInventoryPlugin.sendErrorMsg(sender, "Target player does not exist or is offline.");
            return true;
        }

        ItemStack[] inventoryContents = RestoreInventoryCommand.inventoryContents.getOrDefault(args[0], null);

        if (inventoryContents == null) {
            KeepInventoryPlugin.sendErrorMsg(sender, "Target player does not have a stored inventory. Or they don't exist.");
            return true;
        }

        Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER, Component.text("Your previous inventory"));
        inventory.setContents(inventoryContents);

        if (args.length == 1 || !Objects.equals(args[1], "true")) {
            assert targetPlayer != null; // Already checked above
            targetPlayer.openInventory(inventory);
        } else {
            commandSender.openInventory(inventory);
        }

        return true;
    }
}
