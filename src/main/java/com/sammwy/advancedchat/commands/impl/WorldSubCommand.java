package com.sammwy.advancedchat.commands.impl;

import com.sammwy.advancedchat.commands.Command;
import com.sammwy.advancedchat.commands.CommandContext;
import com.sammwy.advancedchat.commands.CommandListener;

@Command(name = "world")
public class WorldSubCommand extends CommandListener {
  @Override
  public void onExecute(CommandContext ctx) {
    ctx.getExecutor().sendI18nMessage("world");
  }
}
