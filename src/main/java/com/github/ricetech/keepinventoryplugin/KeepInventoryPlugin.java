package com.github.ricetech.keepinventoryplugin;

import com.github.ricetech.keepinventoryplugin.commands.RestoreInventoryCommand;
import com.github.ricetech.keepinventoryplugin.listeners.PlayerInventoryStorageListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class KeepInventoryPlugin extends JavaPlugin {
    public static final String RESTORE_INVENTORY_COMMAND_ALIAS = "restoreinventory";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Keep Inventory Plugin enabled!");

        // Listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerInventoryStorageListener(), this);

        // Commands
        this.getCommand(RESTORE_INVENTORY_COMMAND_ALIAS).setExecutor(new RestoreInventoryCommand());

        // Tab Completers
        this.getCommand(RESTORE_INVENTORY_COMMAND_ALIAS).setTabCompleter(new PluginTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Keep Inventory Plugin disabled!");
    }

    public static void sendErrorMsg(CommandSender target, String message) {
        target.sendMessage(ChatColor.RED + "Error: " + message);
    }
}
