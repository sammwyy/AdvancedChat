package com.sammwy.advancedchat.api;

import org.bukkit.entity.Player;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.players.ChatPlayer;

public class ChatAPI {
    private static AdvancedChat plugin;

    public ChatAPI(AdvancedChat plugin) {
        ChatAPI.plugin = plugin;
    }

    public static ChatPlayer getPlayer(Player player) {
        return plugin.getPlayerManager().getPlayer(player);
    }
}
