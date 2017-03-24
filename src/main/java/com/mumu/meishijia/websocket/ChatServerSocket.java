package com.mumu.meishijia.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket的处理类
 * Created by Administrator on 2017/3/24.
 */
@ServerEndpoint(value = "/chatServer")
public class ChatServerSocket {
    //用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //用来存储所有socket连接的集合
    //它是线程安全的无序的集合，可以将它理解成线程安全的HashSet，从这段百度到的介绍可以看出这好像挺有讲究的，没搞懂前还是用这个集合
    private static CopyOnWriteArraySet<ChatServerSocket> webSocketSet = new CopyOnWriteArraySet<ChatServerSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //记录连接到服务端的用户的用户名
    private String userid;
    //request的session
    private HttpSession httpSession;

    //在线列表,记录用户名称
    private static List<String> onlineList = new ArrayList<String>();
    //用户名和webSocket的session绑定的路由表
    private static Map<String, Session> socketSessionMap = new HashMap<String, Session>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig){
        this.session = session;
        webSocketSet.add(this);
        httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());

    }

    @OnMessage
    public void onMessage(String message){
        System.out.println("Message from" + session.getId() + ":" + message);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        System.out.println("Session" + session.getId() + "has closed!");
    }

    @OnError
    public void onError(Throwable t){
        t.printStackTrace();
    }

    private void broadcast(){}
}
