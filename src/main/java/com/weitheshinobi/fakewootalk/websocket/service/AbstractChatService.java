package com.weitheshinobi.fakewootalk.websocket.service;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;

public abstract class AbstractChatService {

    protected static final String SYSTEM_MESSAGE_USER_LEFT = "系統訊息：對方離開了，請按離開按鈕回到首頁";
    protected static final String SYSTEM_MESSAGE_START_CHAT = "開始聊天囉！";
    protected static final String SYSTEM_MESSAGE_SEARCHING = "系統訊息：正在幫您配對中…";

    protected Session mSession;
    protected ChatRoom mChatRoom;
    protected Session anotherUser;

    abstract public void onOpen(Session session) throws IOException;

    public void onMessage(String message, Session session) throws IOException {
        boolean isChatRoomFull = mChatRoom.getUserSession1() != null && mChatRoom.getUserSession2() != null;
        if (isChatRoomFull) {
            anotherUser = getAnotherUser(mChatRoom);
            anotherUser.getBasicRemote().sendText(message);
        } else {
            session.getBasicRemote().sendText(SYSTEM_MESSAGE_SEARCHING);
        }
    }

    public void onClose(Session session) throws IOException {
        if (anotherUser == null) {
            anotherUser = getAnotherUser(mChatRoom);
        } else if (anotherUser.isOpen()) {
            anotherUser.getBasicRemote().sendText(SYSTEM_MESSAGE_USER_LEFT);
            anotherUser.close();
        }
    }

    public abstract void onError(Session session, Throwable throwable) throws IOException;

    protected Session getAnotherUser(ChatRoom chatRoom) {
        return (chatRoom.getUserSession1() == mSession) ?
                chatRoom.getUserSession2() :
                chatRoom.getUserSession1();
    }

}
