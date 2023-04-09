package com.sammwy.advancedchat.automod.expressions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.ConfigurationSection;

import com.sammwy.advancedchat.players.ChatPlayer;
import com.sammwy.advancedchat.utils.ListUtils;

public class Expression {
    private boolean enabled;
    private String bypassPermission;
    private String message;
    private Pattern regex;
    private List<String> whitelist;
    private List<String> blacklist;
    private boolean ignoreCase;
    private boolean ignoreSpecials;
    private boolean matchEntireWord;

    public Expression(ConfigurationSection section) {
        this.enabled = section.getBoolean("enabled");
        this.bypassPermission = section.getString("bypass");
        this.message = section.getString("message");
        this.whitelist = section.getStringList("whitelist");
        this.blacklist = section.getStringList("blacklist.items");
        this.ignoreCase = section.getBoolean("blacklist.ignore-case");
        this.ignoreSpecials = section.getBoolean("blacklist.ignore-specials");
        this.matchEntireWord = section.getBoolean("blacklist.match-entire-word");

        String regex_str = section.getString("regex");
        if (regex_str != null) {
            this.regex = Pattern.compile(regex_str);
        }
    }

    public boolean check(ChatPlayer player, String chatMessage) {
        if (this.enabled) {
            if (!player.hasPermission(bypassPermission)) {
                if (!this.pass(chatMessage)) {
                    player.sendMessage(this.message);
                    return false;
                }
            }
        }

        return true;
    }

    public boolean pass(String chatMessage) {
        if (this.regex != null) {
            Matcher matcher = this.regex.matcher(chatMessage);
            return !matcher.results().anyMatch((result) -> {
                return ListUtils.containsIgnoreCase(result.group(), this.whitelist);
            });
        } else if (this.blacklist != null) {
            if (this.ignoreSpecials) {
                chatMessage = chatMessage
                        .replace("4", "a")
                        .replace("3", "e")
                        .replace("1", "i")
                        .replace("0", "o")
                        .replace("7", "t")
                        .replace("v", "u");
            }

            if (this.ignoreCase) {
                chatMessage = chatMessage.toLowerCase();
            }

            for (String word : this.blacklist) {
                word = this.ignoreCase ? word.toLowerCase() : word;

                if (this.matchEntireWord) {
                    for (String strWord : chatMessage.split(" ")) {
                        if (strWord.equals(word)) {
                            return false;
                        }
                    }
                } else {
                    if (chatMessage.contains(word)) {
                        return false;
                    } else {
                        continue;
                    }
                }
            }
        }

        return true;
    }
}
