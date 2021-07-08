package com.weitheshinobi.fakewootalk.websocket;

import com.weitheshinobi.fakewootalk.websocket.pojo.ChatRoom;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ServerEndpoint("/chat")
@Component
public class ChatEndpoint {

    private static final String SYSTEM_MESSAGE_USER_LEFT = "系統訊息：對方離開了，請按離開按鈕回到首頁";
    private static final String SYSTEM_MESSAGE_START_CHAT = "開始聊天囉！";
    private static final String SYSTEM_MESSAGE_SEARCHING = "系統訊息：正在幫您配對中…";

    private static Queue<ChatRoom> allChatRooms = new ConcurrentLinkedQueue<>();

    //    通過該物件發送訊息給指定客戶端
    private Session session;
    private ChatRoom myChatRoom;
    private Session anotherUser;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;

        if (allChatRooms.size() == 0) {
            myChatRoom = ChatRoom.getInstance(session);
            allChatRooms.add(myChatRoom);
        } else {
//            拿到聊天室時，可能user1已經離開了，此時直接關閉(edge case)
            myChatRoom = allChatRooms.poll();
            if (myChatRoom.getUserSession1().isOpen()){
                myChatRoom.setUserSession2(session);
                myChatRoom.getUserSession1().getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_START_CHAT);
            } else {
                session.getBasicRemote().sendText(SYSTEM_MESSAGE_USER_LEFT);
                session.close();
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        boolean isChatRoomFull = myChatRoom.getUserSession1() != null && myChatRoom.getUserSession2() != null;
        if (isChatRoomFull) {
            anotherUser = setAnotherUser(myChatRoom);
            anotherUser.getBasicRemote().sendText(message);
        } else {
            session.getBasicRemote().sendText(SYSTEM_MESSAGE_SEARCHING);
        }

    }
    
    private Session setAnotherUser(ChatRoom chatRoom) {
        if (chatRoom.getUserSession1() == session) {
            return chatRoom.getUserSession2();
        } else {
            return chatRoom.getUserSession1();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Close session Id ：" + session.getId());

        if(anotherUser == null){
            anotherUser = setAnotherUser(myChatRoom);
        }

        if (anotherUser != null) {
            if(anotherUser.isOpen()) {
                anotherUser.getBasicRemote().sendText(SYSTEM_MESSAGE_USER_LEFT);
                anotherUser.close();
            }
        }

    }

}
