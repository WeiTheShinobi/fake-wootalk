package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

public class SecretChatService extends AbstractChatService {

    private String mSecret;

    @Override
    public void onOpen(Queue chatRoomQueue,Map chatRoomMap, String secret, Session session, EndpointConfig config) throws IOException {
        mSession = session;

        if (chatRoomMap.get(secret) == null) {
            mChatRoom = ChatRoom.getInstance(session);
            chatRoomMap.put(secret, mChatRoom);
            mSecret = secret;
        } else {
            mChatRoom = (ChatRoom) chatRoomMap.get(secret);
            chatRoomMap.remove(secret);

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
    public void onClose(Session session, Queue queue, Map map) throws IOException {
        onClose(session);
        synchronized (map) {
            if (map.size() > 0) {
                if (map.get(mSecret) == mChatRoom) map.remove(mSecret);
            }
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) throws IOException {

    }


}
