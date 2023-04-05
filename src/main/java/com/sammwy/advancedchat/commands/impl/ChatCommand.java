package com.sammwy.advancedchat.commands.impl;

import com.sammwy.advancedchat.commands.Command;
import com.sammwy.advancedchat.commands.CommandContext;
import com.sammwy.advancedchat.commands.CommandListener;
import com.sammwy.advancedchat.commands.impl.admin.ChatClearCommand;

@Command(name = "chat", permission = "chat.admin")
public class ChatCommand extends CommandListener {
  public ChatCommand() {
    this.addSubcommand(new ChatClearCommand());
  }

  @Override
  public void onExecute(CommandContext ctx) {
    ctx.getExecutor().sendI18nMessage("help");
  }
}
