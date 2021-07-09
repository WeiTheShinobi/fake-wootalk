package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;


public class ChatServiceImpl implements ChatService {

    private static final String SYSTEM_MESSAGE_USER_LEFT = "系統訊息：對方離開了，請按離開按鈕回到首頁";
    private static final String SYSTEM_MESSAGE_START_CHAT = "開始聊天囉！";
    private static final String SYSTEM_MESSAGE_SEARCHING = "系統訊息：正在幫您配對中…";

    private Session session;
    private ChatRoom mChatRoom;
    private Session anotherUser;

    public static ChatServiceImpl getInstance() {
        return new ChatServiceImpl();
    }

    @Override
    public void onOpen(Queue chatRoomQueue, Session session, EndpointConfig config) throws IOException {
        this.session = session;

        if (chatRoomQueue.size() == 0) {
            mChatRoom = ChatRoom.getInstance(session);
            chatRoomQueue.add(mChatRoom);
        } else {
//            拿到聊天室時，可能user1已經離開了，此時直接關閉(edge case)
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
    public void onOpen(Map chatRoomMap, String secret, Session session, EndpointConfig config) throws IOException {

    }

    @Override
    public void onMessage(String message, Session session) throws IOException {
        boolean isChatRoomFull = mChatRoom.getUserSession1() != null && mChatRoom.getUserSession2() != null;
        if (isChatRoomFull) {
            anotherUser = getAnotherUser(mChatRoom);
            anotherUser.getBasicRemote().sendText(message);
        } else {
            session.getBasicRemote().sendText(SYSTEM_MESSAGE_SEARCHING);
        }

    }

    @Override
    public void onClose(Session session) throws IOException {
        if(anotherUser == null){
            anotherUser = getAnotherUser(mChatRoom);
        }

        if (anotherUser != null) {
            if(anotherUser.isOpen()) {
                anotherUser.getBasicRemote().sendText(SYSTEM_MESSAGE_USER_LEFT);
                anotherUser.close();
            }
        }

    }

    @Override
    public void onClose(Session session, Queue queue, Map map) throws IOException {
        onClose(session);
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

    private Session getAnotherUser(ChatRoom chatRoom) {
        if (chatRoom.getUserSession1() == session) {
            return chatRoom.getUserSession2();
        } else {
            return chatRoom.getUserSession1();
        }
    }

    Session getSession() {
        return session;
    }

    void setSession(Session session) {
        this.session = session;
    }

    ChatRoom getmChatRoom() {
        return mChatRoom;
    }

    void setmChatRoom(ChatRoom mChatRoom) {
        this.mChatRoom = mChatRoom;
    }

    Session getAnotherUser() {
        return anotherUser;
    }

    void setAnotherUser(Session anotherUser) {
        this.anotherUser = anotherUser;
    }
}
