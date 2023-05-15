package com.github.ricetech.keepinventoryplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class KeepInventoryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void sendErrorMsg(CommandSender target, String message) {
        target.sendMessage(ChatColor.RED + "Error: " + message);
    }
}
