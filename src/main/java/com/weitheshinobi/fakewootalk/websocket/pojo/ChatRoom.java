package com.weitheshinobi.fakewootalk.websocket.pojo;

import javax.websocket.Session;

public class ChatRoom {
    private Session userSession1;
    private Session userSession2;

    public static ChatRoom getInstance(Session userSession1) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserSession1(userSession1);
        return chatRoom;
    }

    public Session getUserSession1() {
        return userSession1;
    }

    public void setUserSession1(Session userSession1) {
        this.userSession1 = userSession1;
    }

    public Session getUserSession2() {
        return userSession2;
    }

    public void setUserSession2(Session userSession2) {
        this.userSession2 = userSession2;
    }
}
