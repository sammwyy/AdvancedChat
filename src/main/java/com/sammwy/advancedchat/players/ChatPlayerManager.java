package com.sammwy.advancedchat.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import com.sammwy.advancedchat.AdvancedChat;

public class ChatPlayerManager {
    private AdvancedChat plugin;

    private Map<Player, ChatPlayer> players;

    public ChatPlayerManager(AdvancedChat plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    public ChatPlayer addPlayer(@Nonnull Player bukkitPlayer) {
        ChatPlayer player = new ChatPlayer(this.plugin, bukkitPlayer);
        this.players.put(bukkitPlayer, player);
        return player;
    }

    public ChatPlayer removePlayer(Player bukkitPlayer) {
        return this.players.remove(bukkitPlayer);
    }

    public ChatPlayer getPlayer(Player bukkitPlayer) {
        return this.players.get(bukkitPlayer);
    }

    public ChatPlayer getPlayer(String name) {
        Player bukkitPlayer = this.plugin.getServer().getPlayerExact(name);
        if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
            return this.getPlayer(bukkitPlayer);
        } else {
            return null;
        }
    }

    public Collection<ChatPlayer> getPlayers() {
        return this.players.values();
    }

    public void clear() {
        this.players.clear();
    }

    public void addAll() {
        for (Player bukkitPlayer : this.plugin.getServer().getOnlinePlayers()) {
            if (bukkitPlayer != null) {
                this.addPlayer(bukkitPlayer).download();
            }
        }
    }
}