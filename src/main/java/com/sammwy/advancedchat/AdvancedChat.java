package com.sammwy.advancedchat;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sammwy.advancedchat.api.ChatAPI;
import com.sammwy.advancedchat.api.events.ChatEvent;
import com.sammwy.advancedchat.automod.Automod;
import com.sammwy.advancedchat.commands.CommandExecutor;
import com.sammwy.advancedchat.commands.CommandListener;
import com.sammwy.advancedchat.commands.impl.ChatCommand;
import com.sammwy.advancedchat.config.ConfigManager;
import com.sammwy.advancedchat.config.Configuration;
import com.sammwy.advancedchat.listeners.AsyncPlayerChatListener;
import com.sammwy.advancedchat.listeners.PlayerJoinListener;
import com.sammwy.advancedchat.listeners.PlayerQuitListener;
import com.sammwy.advancedchat.players.ChatPlayerManager;
import com.sammwy.libi18n.LanguageManager;

public class AdvancedChat extends JavaPlugin {
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private ChatPlayerManager playerManager;

    private Automod automod;
    private CommandExecutor console;

    private void addCommand(CommandListener command) {
        command.register(this, false);
    }

    private void addListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public boolean callEvent(ChatEvent event) {
        this.getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    @Override
    public void onEnable() {
        // Initialize API
        new ChatAPI(this);

        // Instantiate managers.
        this.configManager = new ConfigManager(this);
        this.languageManager = LanguageManager.forBukkit(this, "lang");
        this.playerManager = new ChatPlayerManager(this);

        // Load translation.
        this.languageManager.extractIfEmpty("lang/");
        this.languageManager.withDefault(getConfig().getString("settings.default-lang"));

        // Load data.
        this.languageManager.tryLoadLanguages();
        this.playerManager.addAll();

        // Register listeners.
        this.addListener(new AsyncPlayerChatListener(this));
        this.addListener(new PlayerJoinListener(this));
        this.addListener(new PlayerQuitListener(this));

        // Register commands.
        this.addCommand(new ChatCommand());

        // Get console.
        this.automod = new Automod(this);
        this.console = new CommandExecutor(this, this.getServer().getConsoleSender());
    }

    // Configuration getters
    public Configuration getConfig(String file) {
        return this.configManager.getConfig(file);
    }

    public Configuration getConfig() {
        return this.getConfig("config.yml");
    }

    // Managers getters
    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public ChatPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    // Others getters
    public Automod getAutomod() {
        return this.automod;
    }

    public CommandExecutor getConsole() {
        return this.console;
    }

    public boolean hasPlugin(String pluginName) {
        Plugin plugin = this.getServer().getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}