package com.github.ricetech.keepinventoryplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PluginTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<List<String>> tabComplete;

        // The list of online players needs to be regenerated every time the function is called to stay up-to-date
        List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();

        switch (alias) {
            case KeepInventoryPlugin.RESTORE_INVENTORY_COMMAND_ALIAS -> tabComplete = Arrays.asList(players, List.of(""), Arrays.asList("true", "false"));
            default -> tabComplete = null;
        }

        if (tabComplete == null) {
            return Collections.emptyList();
        }

        String partialArg = args.length > 0 ? args[args.length - 1] : "";
        int argNum = args.length > 0 ? args.length - 1 : 0;

        // Catch if user tries to autocomplete a command with no arguments or if they autocomplete more arguments
        if (argNum >= tabComplete.size()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(tabComplete.get(argNum)).stream()
                .filter(option -> (partialArg.isEmpty() || option.toLowerCase().startsWith(partialArg.toLowerCase())))
                .collect(Collectors.toList());
    }
}
