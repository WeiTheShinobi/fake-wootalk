package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SecretChatServiceImpl extends ChatService {

    protected static Map<String, ChatRoom> chatRoomsMap = new ConcurrentHashMap<>();

    private String mSecret;

    public SecretChatServiceImpl(String mSecret) {
        this.mSecret = mSecret;
    }

    @Override
    public void onOpen(Session session) throws IOException {
        mSession = session;

        if (chatRoomsMap.get(mSecret) == null) {
            mChatRoom = ChatRoom.getInstance(session);
            chatRoomsMap.put(mSecret, mChatRoom);
        } else {
            mChatRoom = chatRoomsMap.get(mSecret);
            chatRoomsMap.remove(mSecret);

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
    public void onClose() throws IOException {
        super.onClose();
        if (chatRoomsMap.get(mSecret) == mChatRoom) chatRoomsMap.remove(mSecret);
    }

}
