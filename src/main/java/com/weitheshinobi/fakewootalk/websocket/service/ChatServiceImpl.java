package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ChatServiceImpl extends ChatService {

    private static final Queue<ChatRoom> chatRoomsQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void onOpen(Session session) throws IOException {
        mSession = session;

        if (chatRoomsQueue.size() == 0) {
            mChatRoom = ChatRoom.getInstance(session);
            chatRoomsQueue.add(mChatRoom);
        } else {
            mChatRoom = chatRoomsQueue.poll();
            mChatRoom.setUserSession2(session);

            anotherUser = mChatRoom.getUserSession1();
            anotherUser.getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
            session.getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
        }
    }

    @Override
    public void onClose() throws IOException {
        super.onClose();
        if (chatRoomsQueue.size() > 0) {
            ChatRoom chatRoom = chatRoomsQueue.element();
            if (chatRoom == mChatRoom) chatRoomsQueue.poll();
        }
    }

}
