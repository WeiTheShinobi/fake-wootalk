package com.weitheshinobi.fakewootalk.websocket;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;
import com.weitheshinobi.fakewootalk.websocket.service.ChatService;
import com.weitheshinobi.fakewootalk.websocket.service.ChatServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ServerEndpoint("/chat")
@Component
public class ChatEndpoint {

    private static Queue<ChatRoom> allChatRooms = new ConcurrentLinkedQueue<>();

    private ChatService chatServiceImpl;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        chatServiceImpl = ChatServiceImpl.getInstance();
        chatServiceImpl.onOpen(allChatRooms, session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        chatServiceImpl.onMessage(message, session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        chatServiceImpl.onClose(session);
    }


}
