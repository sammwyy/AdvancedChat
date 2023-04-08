package com.sammwy.advancedchat.players;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.commands.CommandExecutor;
import com.sammwy.libchat.LibChatBukkit;
import com.sammwy.libchat.chat.Component;
import com.sammwy.libi18n.Language;

import me.clip.placeholderapi.PlaceholderAPI;

public class ChatPlayer extends CommandExecutor {
    private @Nonnull Player bukkitPlayer;
    private ChatPlayerState state;

    public ChatPlayer(AdvancedChat plugin, @Nonnull Player bukkitPlayer) {
        super(plugin, bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
        this.state = new ChatPlayerState();
    }

    public void download() {
        // Descargar datos del jugador de la DB.
        // Este metodo es llamado cuando el jugador se une al servidor.
    }

    @Override
    public String formatMessage(String message) {
        String output = super.formatMessage(message);

        if (this.getPlugin().hasPlugin("PlaceholderAPI")) {
            output = PlaceholderAPI.setPlaceholders(this.getBukkitPlayer(), output);
        }

        return output
                .replace("{display_name}", this.bukkitPlayer.getDisplayName())
                .replace("{name}", this.getName());
    }

    public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    @Override
    public Language getLang() {
        return this.getPlugin().getLanguageManager().getLanguage(this.bukkitPlayer);
    }

    public String getName() {
        return this.bukkitPlayer.getName();
    }

    public String getLowerName() {
        return this.getName().toLowerCase();
    }

    public ChatPlayerState getState() {
        return this.state;
    }

    public boolean isOnline() {
        return this.bukkitPlayer != null && this.bukkitPlayer.isOnline();
    }

    public void sendRawMessage(String json) {
        if (json != null) {
            LibChatBukkit.sendMessage(bukkitPlayer, json);
        }
    }

    public void sendActionBar(@Nonnull String text) {
        if (text != null) {
            LibChatBukkit.sendActionbar(bukkitPlayer, text);
        }
    }

    public void sendMessage(Component component) {
        if (component != null) {
            LibChatBukkit.sendMessage(bukkitPlayer, component);
        }
    }
}