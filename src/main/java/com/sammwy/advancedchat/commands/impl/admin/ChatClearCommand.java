package com.sammwy.advancedchat.commands.impl.admin;

import com.sammwy.advancedchat.AdvancedChat;
import com.sammwy.advancedchat.commands.Command;
import com.sammwy.advancedchat.commands.CommandContext;
import com.sammwy.advancedchat.commands.CommandListener;
import com.sammwy.advancedchat.config.Configuration;
import com.sammwy.advancedchat.players.ChatPlayer;

@Command(name = "clear", permission = "chat.admin.clear")
public class ChatClearCommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        AdvancedChat plugin = ctx.getPlugin();
        Configuration config = plugin.getConfig();
        boolean all = "all".equalsIgnoreCase(ctx.getArguments().getString(0));

        String clearMessage = "\n".repeat(config.getInt("clear.lines"));

        for (ChatPlayer player : plugin.getPlayerManager().getPlayers()) {
            if (!all && !player.hasPermission(config.getString("clear.bypass"))) {
                player.sendMessage(clearMessage);
                player.sendI18nMessage("chat.cleared-by-admin");
            }
        }

        ctx.getPlayer().sendI18nMessage("chat.success");
    }
}
