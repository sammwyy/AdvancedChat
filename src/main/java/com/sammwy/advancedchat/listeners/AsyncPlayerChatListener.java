package com.sammwy.advancedchat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.players.ChatPlayer;
import com.sammwy.libchat.chat.Component;
import com.sammwy.libchat.chat.TextComponent;
import com.sammwy.libchat.events.ClickEvent;
import com.sammwy.libchat.events.ClickEventType;
import com.sammwy.libchat.events.HoverEvent;
import com.sammwy.libchat.events.HoverEventType;

public class AsyncPlayerChatListener implements Listener {
    private AdvancedChat plugin;

    public AsyncPlayerChatListener(AdvancedChat plugin) {
        this.plugin = plugin;
    }

    private Component messageToComponent(ChatPlayer player, ChatPlayer recipent, String format) {
        if (!format.contains("<profile>")) {
            return new TextComponent(format);
        }

        String text = format.split("<profile>")[1].split("</profile>")[0];
        String profile = player
                .formatMessage(recipent.formatTranslationKeys(plugin.getConfig().getString("profile.hover")));
        String[] parts = format.split("<profile>" + text + "</profile>");

        Component component = new TextComponent(parts[0]);
        component.addChild(new TextComponent(text)
                .setClickEvent(new ClickEvent(
                        ClickEventType.valueOf(plugin.getConfig().getString("profile.click.type")),
                        plugin.getConfig().getString("profile.click.action")
                                .replace("{clicked_player}", player.getName())
                                .replace("{player}", recipent.getName())))
                .setHoverEvent(new HoverEvent(HoverEventType.SHOW_TEXT, new TextComponent(profile))));
        component.addChild(new TextComponent(parts[1]));
        return component;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        ChatPlayer player = this.plugin.getPlayerManager().getPlayer(e.getPlayer());

        // Restrinction.
        if (this.plugin.getConfig().getBoolean("restrinction.enabled")) {
            String permission = this.plugin.getConfig().getString("restrinction.permission");

            if (!player.hasPermission(permission)) {
                player.sendI18nMessage("restricted");
                e.setCancelled(true);
                return;
            }
        }

        // Formatting.
        String format = this.plugin.getConfig().getString("format");
        if (format != null && !format.isEmpty()) {
            String message = player.formatMessage(format).replace("{message}", e.getMessage());

            for (Player chatRecipent : e.getRecipients()) {
                ChatPlayer recipent = this.plugin.getPlayerManager().getPlayer(chatRecipent);
                Component component = this.messageToComponent(player, recipent, message);
                recipent.sendMessage(component);
            }

            e.setCancelled(true);
        }
    }
}
