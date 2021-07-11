package com.weitheshinobi.fakewootalk.websocket;

import com.weitheshinobi.fakewootalk.websocket.config.GetHttpSessionConfigurator;
import com.weitheshinobi.fakewootalk.websocket.service.AbstractChatServiceSimpleFactory;
import com.weitheshinobi.fakewootalk.websocket.service.AbstractChatService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {

    private AbstractChatService chatService;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        String secret = getSecretFromHttpSession(config);
        chatService = AbstractChatServiceSimpleFactory.createChatService(secret);
        chatService.onOpen(session);
    }

    private String getSecretFromHttpSession(EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String secret = (String) httpSession.getAttribute("secret");
        httpSession.removeAttribute("secret");
        return secret;
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        chatService.onMessage(message, session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        chatService.onClose(session);
    }

}
