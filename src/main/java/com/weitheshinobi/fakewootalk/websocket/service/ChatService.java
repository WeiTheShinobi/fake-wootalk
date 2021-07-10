package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;


public class ChatService extends AbstractChatService {

    @Override
    public void onOpen(Queue chatRoomQueue,Map chatRoomMap, String secret, Session session, EndpointConfig config) throws IOException {
        mSession = session;

        if (chatRoomQueue.size() == 0) {
            mChatRoom = ChatRoom.getInstance(session);
            chatRoomQueue.add(mChatRoom);
        } else {
            mChatRoom = (ChatRoom) chatRoomQueue.poll();
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
    public void onClose(Session session, Queue queue, Map map) throws IOException {
        super.onClose(session);
        synchronized (queue){
            if (queue.size() > 0){
                ChatRoom chatRoom = (ChatRoom) queue.element();
                if(chatRoom == mChatRoom) queue.poll();
            }
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) throws IOException {

    }

}
