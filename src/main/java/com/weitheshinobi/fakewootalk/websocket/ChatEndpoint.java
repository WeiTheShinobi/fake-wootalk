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

    private static Queue<ChatRoom> allChatRooms = new ConcurrentLinkedQueue<>();

    //    通過該物件發送訊息給指定客戶端
    private Session session;
    private ChatRoom myChatRoom;
    private Session anotherUser;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

        if (allChatRooms.size() == 0) {
            myChatRoom = ChatRoom.getInstance(session);
            allChatRooms.add(myChatRoom);
        } else {
            myChatRoom = allChatRooms.poll();
            myChatRoom.setUserSession2(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if (myChatRoom.getUserSession1() != null && myChatRoom.getUserSession2() != null){
            anotherUser = setAnotherUser(myChatRoom);
            anotherUser.getBasicRemote().sendText(message);
        } else {
            session.getBasicRemote().sendText("尋找中…");
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
        System.out.println(session.getId());
        anotherUser.getBasicRemote().sendText("系統訊息：對方離開了，請按離開按鈕回到首頁");
    }

}
