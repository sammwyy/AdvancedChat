package com.sammwy.advancedchat.automod.expressions;

import java.util.ArrayList;
import java.util.List;

import com.sammwy.advancedchat.config.Configuration;
import com.sammwy.advancedchat.players.ChatPlayer;

public class ExpressionManager {
    private Configuration config;
    private List<Expression> expressions;

    public ExpressionManager(Configuration config) {
        this.config = config;
        this.expressions = new ArrayList<>();
    }

    public void loadExpressions() {
        this.expressions.clear();

        for (String key : config.getKeys(false)) {
            this.expressions.add(new Expression(config.getConfigurationSection(key)));
        }
    }

    public boolean check(ChatPlayer player, String message) {
        return expressions.stream().allMatch((exp) -> {
            return exp.check(player, message);
        });
    }
}
