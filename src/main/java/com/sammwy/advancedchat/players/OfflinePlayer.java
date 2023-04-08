package com.sammwy.advancedchat.players;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import com.sammwy.advancedchat.AdvancedChat;

public class OfflinePlayer extends ChatPlayer {
    private String username;

    public OfflinePlayer(AdvancedChat plugin, @Nonnull Player bukkitPlayer, String username) {
        super(plugin, bukkitPlayer);
        this.username = username.toLowerCase();
    }

    @Override
    public String getLowerName() {
        return this.username;
    }
}