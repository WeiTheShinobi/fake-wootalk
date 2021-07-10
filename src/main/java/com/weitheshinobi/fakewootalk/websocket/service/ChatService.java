package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ChatService extends AbstractChatService {

    protected static Queue<ChatRoom> chatRoomsQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void onOpen(String secret, Session session, EndpointConfig config) throws IOException {
        mSession = session;

        if (chatRoomsQueue.size() == 0) {
            mChatRoom = ChatRoom.getInstance(session);
            chatRoomsQueue.add(mChatRoom);
        } else {
            mChatRoom = chatRoomsQueue.poll();
            if (mChatRoom.getUserSession1().isOpen()){
                mChatRoom.setUserSession2(session);

                mChatRoom.getUserSession1().getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
            } else {
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_USER_LEFT);
                session.close();
            }
        }

    }

    @Override
    public void onClose(Session session) throws IOException {
        super.onClose(session);
        if (chatRoomsQueue.size() > 0) {
            ChatRoom chatRoom = chatRoomsQueue.element();
            if(chatRoom == mChatRoom) chatRoomsQueue.poll();
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) throws IOException {

    }

}
