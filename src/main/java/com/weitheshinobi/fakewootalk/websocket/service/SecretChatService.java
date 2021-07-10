package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SecretChatService extends AbstractChatService {

    protected static Map<String, ChatRoom> chatRoomsMap = new ConcurrentHashMap<>();

    private String mSecret;

    @Override
    public void onOpen(String secret, Session session) throws IOException {
        mSession = session;
        mSecret = secret;

        if (chatRoomsMap.get(secret) == null) {
            mChatRoom = ChatRoom.getInstance(session);
            chatRoomsMap.put(secret, mChatRoom);
        } else {
            mChatRoom = chatRoomsMap.get(secret);
            chatRoomsMap.remove(secret);

            anotherUser = mChatRoom.getUserSession1();
            if (anotherUser.isOpen()) {
                mChatRoom.getUserSession1().getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);

                mChatRoom.setUserSession2(session);
            } else {
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_USER_LEFT);
                session.close();
            }
        }
    }

    @Override
    public void onClose(Session session) throws IOException {
        super.onClose(session);
        if (chatRoomsMap.get(mSecret) == mChatRoom) chatRoomsMap.remove(mSecret);
    }

    @Override
    public void onError(Session session, Throwable throwable) throws IOException {

    }


}
