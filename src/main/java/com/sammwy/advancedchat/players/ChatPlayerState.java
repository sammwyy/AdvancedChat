package com.sammwy.advancedchat.players;

public class ChatPlayerState {
    public String lastMessage;
    public long lastMessageTimestamp;

    public ChatPlayerState() {
        this.lastMessage = "";
        this.lastMessageTimestamp = 0;
    }
}
