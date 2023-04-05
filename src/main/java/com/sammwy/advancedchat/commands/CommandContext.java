package com.sammwy.advancedchat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.errors.BadArgumentException;
import com.sammwy.advancedchat.errors.MaterialNotFoundException;
import com.sammwy.advancedchat.errors.PlayerOfflineException;
import com.sammwy.advancedchat.errors.SoundNotFoundException;
import com.sammwy.advancedchat.errors.WorldNotFoundException;
import com.sammwy.advancedchat.players.ChatPlayer;

public class CommandContext {
    private AdvancedChat plugin;
    private CommandExecutor executor;
    private CommandArguments arguments;

    public CommandContext(AdvancedChat plugin, CommandSender sender, Argument[] requiredArguments) {
        if (sender instanceof Player) {
            this.executor = plugin.getPlayerManager().getPlayer((Player) sender);
        } else {
            this.executor = new CommandExecutor(plugin, sender);
        }

        this.plugin = plugin;
        this.arguments = new CommandArguments(plugin, requiredArguments);
    }

    public void parseArguments(String[] args) throws BadArgumentException, PlayerOfflineException,
            WorldNotFoundException, MaterialNotFoundException, SoundNotFoundException {
        this.arguments.parse(args);
    }

    public AdvancedChat getPlugin() {
        return this.plugin;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }

    public ChatPlayer getPlayer() {
        return (ChatPlayer) this.executor;
    }

    public boolean isPlayer() {
        return this.executor.isPlayer();
    }

    public CommandArguments getArguments() {
        return this.arguments;
    }
}