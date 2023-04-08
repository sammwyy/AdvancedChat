package com.sammwy.advancedchat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.players.ChatPlayer;

public class PlayerJoinListener implements Listener {
  private AdvancedChat plugin;

  public PlayerJoinListener(AdvancedChat plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player bukkitPlayer = e.getPlayer();
    if (bukkitPlayer != null) {
      ChatPlayer player = this.plugin.getPlayerManager().addPlayer(bukkitPlayer);
      player.download();
    }
  }
}
