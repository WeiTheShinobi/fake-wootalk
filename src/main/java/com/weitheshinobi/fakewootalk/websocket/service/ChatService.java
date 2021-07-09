package com.weitheshinobi.fakewootalk.websocket.service;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;

public interface ChatService {

    void onOpen(Queue chatRoomQueue, Session session, EndpointConfig config) throws IOException;

    void onOpen(Map chatRoomMap, String secret, Session session, EndpointConfig config) throws IOException;

    void onMessage(String message, Session session) throws IOException;

    void onClose(Session session) throws IOException;

    void onError(Session session, Throwable throwable) throws IOException;

}
