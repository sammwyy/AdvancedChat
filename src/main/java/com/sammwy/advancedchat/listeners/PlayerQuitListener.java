package com.sammwy.advancedchat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sammwy.advancedchat.AdvancedChat;

public class PlayerQuitListener implements Listener {
  private AdvancedChat plugin;

  public PlayerQuitListener(AdvancedChat plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    this.plugin.getPlayerManager().removePlayer(e.getPlayer());
  }
}
