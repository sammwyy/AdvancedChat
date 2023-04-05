package com.sammwy.advancedchat.commands.impl;

import com.sammwy.advancedchat.commands.Command;
import com.sammwy.advancedchat.commands.CommandContext;
import com.sammwy.advancedchat.commands.CommandListener;

@Command(name = "hello")
public class HelloCommand extends CommandListener {
  public HelloCommand() {
    this.addSubcommand(new WorldSubCommand());
  }

  @Override
  public void onExecute(CommandContext ctx) {
    ctx.getExecutor().sendI18nMessage("hello");
  }
}
