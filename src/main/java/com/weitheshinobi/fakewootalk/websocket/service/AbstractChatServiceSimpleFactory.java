package com.weitheshinobi.fakewootalk.websocket.service;

public class AbstractChatServiceSimpleFactory {

    private AbstractChatServiceSimpleFactory() {
    }

    public static AbstractChatService createChatService(String secret) {
        return (secret != null) ? new SecretChatService(secret) : new ChatService();
    }

}
