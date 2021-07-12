package com.weitheshinobi.fakewootalk.websocket.service;

public class ChatServiceSimpleFactory {

    private ChatServiceSimpleFactory() {
    }

    public static ChatService createChatService(String secret) {
        return (secret != null) ? new SecretChatServiceImpl(secret) : new ChatServiceImpl();
    }

}
