package com.weitheshinobi.fakewootalk.websocket.service;

public class AbstractChatServiceSimpleFactory {
    public static AbstractChatService createChatService(String secret) {
        if (secret != null) {
            return new SecretChatService();
        } else {
            return new ChatService();
        }
    }
}
