package com.sammwy.advancedchat.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.config.Configuration;
import com.sammwy.advancedchat.players.ChatPlayer;
import com.sammwy.advancedchat.utils.RegexUtils;
import com.sammwy.libchat.chat.ChatColor;
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
        Configuration config = this.plugin.getConfig();
        ChatPlayer player = this.plugin.getPlayerManager().getPlayer(e.getPlayer());
        String message = e.getMessage();

        // Restrinction.
        if (config.getBoolean("restrinction.enabled")) {
            String permission = config.getString("restrinction.bypass");

            if (!player.hasPermission(permission)) {
                player.sendI18nMessage("restrincion.message");
                e.setCancelled(true);
                return;
            }
        }

        // Cooldown.
        long last = player.getState().lastMessageTimestamp;
        long current = System.currentTimeMillis();
        long cooldown = config.getLong("cooldown.time");

        if ((current - last) < cooldown && !player.hasPermission(config.getString("cooldown.bypass"))) {
            player.sendI18nMessage("cooldown.message");
            e.setCancelled(true);
            return;
        } else {
            player.getState().lastMessageTimestamp = current;
        }

        // Automod: Message repeat.
        if (config.getBoolean("automod.block-repeated-messages.enabled")) {
            if (!player.hasPermission(config.getString("automod.block-repeated-messages.bypass"))) {
                if (player.getState().lastMessage.equalsIgnoreCase(message)) {
                    player.sendI18nMessage("automod.repeated-message");
                    e.setCancelled(true);
                    return;
                } else {
                    player.getState().lastMessage = message;
                }
            }
        }

        // Automod: Links.
        if (config.getBoolean("automod.block-links.enabled")) {
            if (!player.hasPermission(config.getString("automod.block-links.bypass"))) {
                if (RegexUtils.matchURL(message, config.getStringList("automod.block-links.whitelist"))) {
                    player.sendI18nMessage("automod.links");
                    e.setCancelled(true);
                    return;
                }
            }
        }

        // Colorize.
        if (player.hasPermission("chat.color")) {
            boolean specials = player.hasPermission("chat.color.special");
            message = ChatColor.translateAlternateColorCodes('&', message, specials);
        }

        // Grammar.
        if (config.getBoolean("grammar.append-pediod")) {
            if (message.endsWith(".")) {
                message = message + ".";
            }
        }

        if (config.getBoolean("grammar.remove-caps")) {
            message = message.toLowerCase();
        }

        if (config.getBoolean("grammar.capitalize")) {
            message = StringUtils.capitalize(message);
        }

        // Formatting.
        String format = config.getString("format");
        if (format != null && !format.isEmpty()) {
            String finalMessage = player.formatMessage(format).replace("{message}", message);

            for (Player chatRecipent : e.getRecipients()) {
                ChatPlayer recipent = this.plugin.getPlayerManager().getPlayer(chatRecipent);
                Component component = this.messageToComponent(player, recipent, finalMessage);
                recipent.sendMessage(component);
            }

            e.setCancelled(true);
        }

        // Console logging.
        String consoleFormat = config.getString("format-console");
        if (consoleFormat != null && !consoleFormat.isEmpty()) {
            String finalMessage = player.formatMessage(consoleFormat).replace("{message}", message);
            this.plugin.getConsole().sendMessage(finalMessage);
        }
    }
}
