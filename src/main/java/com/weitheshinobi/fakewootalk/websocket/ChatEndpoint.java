package com.weitheshinobi.fakewootalk.websocket;

import com.weitheshinobi.fakewootalk.websocket.config.GetHttpSessionConfigurator;
import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;
import com.weitheshinobi.fakewootalk.websocket.service.ChatService;
import com.weitheshinobi.fakewootalk.websocket.service.ChatServiceImpl;
import com.weitheshinobi.fakewootalk.websocket.service.SecretChatServiceImpl;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {

    private static Queue<ChatRoom> chatRoomsQueue = new ConcurrentLinkedQueue<>();
    private static Map<String, ChatRoom> chatRoomsMap = new ConcurrentHashMap<>();

    private ChatService chatService;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        String secret = getSecretFromHttpSession(config);

        if (secret != null) {
            chatService = SecretChatServiceImpl.getInstance(ChatServiceImpl.getInstance());
            chatService.onOpen(chatRoomsMap, secret, session, config);
        } else {
            chatService = ChatServiceImpl.getInstance();
            chatService.onOpen(chatRoomsQueue, session, config);
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        chatService.onMessage(message, session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        chatService.onClose(session, chatRoomsQueue, chatRoomsMap);
    }

    private String getSecretFromHttpSession(EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String secret = (String) httpSession.getAttribute("secret");
        httpSession.removeAttribute("secret");
        return secret;
    }


}
