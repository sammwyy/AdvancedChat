package com.sammwy.advancedchat.automod;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.automod.expressions.ExpressionManager;
import com.sammwy.advancedchat.config.Configuration;
import com.sammwy.advancedchat.players.ChatPlayer;

public class Automod {
    private Configuration config;
    private ExpressionManager expressions;

    public Automod(AdvancedChat plugin) {
        this.config = plugin.getConfig();
        this.expressions = new ExpressionManager(plugin.getConfig("expressions.yml"));
        this.expressions.loadExpressions();
    }

    public boolean check(ChatPlayer player, String message) {
        // Expressions.
        if (config.getBoolean("automod.expressions.enabled") && !this.expressions.check(player, message)) {
            return false;
        }

        // Cooldown.
        if (config.getBoolean("automod.cooldown.enabled")) {
            long last = player.getState().lastMessageTimestamp;
            long current = System.currentTimeMillis();
            long cooldown = config.getLong("automod.cooldown.time");

            if ((current - last) < cooldown && !player.hasPermission(config.getString("automod.cooldown.bypass"))) {
                player.sendI18nMessage("automod.cooldown");
                return false;
            } else {
                player.getState().lastMessageTimestamp = current;
            }
        }

        // Message repeat.
        if (config.getBoolean("automod.block-repeated-messages.enabled")) {
            if (!player.hasPermission(config.getString("automod.block-repeated-messages.bypass"))) {
                if (player.getState().lastMessage.equalsIgnoreCase(message)) {
                    player.sendI18nMessage("automod.repeated-message");
                    return false;
                } else {
                    player.getState().lastMessage = message;
                }
            }
        }

        return true;
    }
}
