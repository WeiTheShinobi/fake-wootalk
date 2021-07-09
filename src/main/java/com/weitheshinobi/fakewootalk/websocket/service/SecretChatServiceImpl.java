package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

public class SecretChatServiceImpl implements ChatService {

    private static final String SYSTEM_MESSAGE_USER_LEFT = "系統訊息：對方離開了，請按離開按鈕回到首頁";
    private static final String SYSTEM_MESSAGE_START_CHAT = "開始聊天囉！";

    private ChatServiceImpl chatService;
    private String mSecret;

    public static SecretChatServiceImpl getInstance(ChatServiceImpl chatService) {
        return new SecretChatServiceImpl(chatService);
    }

    private SecretChatServiceImpl(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @Override
    public void onOpen(Queue chatRoomQueue, Session session, EndpointConfig config) throws IOException {

    }

    @Override
    public void onOpen(Map chatRoomMap, String secret, Session session, EndpointConfig config) throws IOException {
        chatService.setmSession(session);

        if (chatRoomMap.get(secret) == null){
            chatService.setmChatRoom(ChatRoom.getInstance(session));
            chatRoomMap.put(secret, chatService.getmChatRoom());
            mSecret = secret;
        } else {
            chatService.setmChatRoom((ChatRoom) chatRoomMap.get(secret));
            chatRoomMap.remove(secret);

            chatService.setAnotherUser(chatService.getmChatRoom().getUserSession1());
            if (chatService.getAnotherUser().isOpen()){
                chatService.getmChatRoom().getUserSession1().getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);

                chatService.getmChatRoom().setUserSession2(session);
            } else {
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_USER_LEFT);
                session.close();
            }
        }
    }

    @Override
    public void onMessage(String message, Session session) throws IOException {
        chatService.onMessage(message, session);
    }

    @Override
    public void onClose(Session session) throws IOException {
        chatService.onClose(session);
    }

    @Override
    public void onClose(Session session, Queue queue, Map map) throws IOException {
        onClose(session);
        synchronized (map) {
            if(map.size() > 0){
                if (map.get(mSecret) == chatService.getmChatRoom()) map.remove(mSecret);
            }
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) throws IOException {

    }


}
