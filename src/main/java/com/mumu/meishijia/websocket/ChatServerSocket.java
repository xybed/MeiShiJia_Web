package com.mumu.meishijia.websocket;

import com.google.gson.Gson;
import com.mumu.meishijia.model.im.MsgJsonModel;
import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.service.im.ISocketService;
import lib.utils.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
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
@ServerEndpoint(value = "/chatServer/{principalId}")
public class ChatServerSocket {
    //用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //用来存储所有socket连接的集合
    //它是线程安全的无序的集合，可以将它理解成线程安全的HashSet，从这段百度到的介绍可以看出这好像挺有讲究的，没搞懂前还是用这个集合
    private static CopyOnWriteArraySet<ChatServerSocket> webSocketSet = new CopyOnWriteArraySet<ChatServerSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //request的session
    private HttpSession httpSession;

    //在线列表,记录用户名称
    private static List<String> onlineList = new ArrayList<String>();
    //用户名和webSocket的session绑定的路由表
    private static Map<String, Session> socketSessionMap = new HashMap<String, Session>();

    @Resource
    private ISocketService socketService;

    @OnOpen
    public void onOpen(@PathParam(value="principalId") String principalId, Session session, EndpointConfig endpointConfig){
        this.session = session;
        webSocketSet.add(this);
        httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
        socketSessionMap.put(principalId, session);
        System.out.println("socket连接成功");
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println(message);
        Gson gson = new Gson();
        MsgJsonModel msgJson = gson.fromJson(message, MsgJsonModel.class);
        //存消息记录到数据库
        int msgId = socketService.insertMessage(msgJson);
        //给msgJson加上msg_id
        msgJson.setMsg_id(msgId);
        //给msgJson加上发送者的头像、备注等
        int fromId = msgJson.getData().getFrom_id();
        User sendUser = socketService.querySendUser(fromId);
        msgJson.getData().setFriend_id(sendUser.getId());
        msgJson.getData().setAvatar(sendUser.getAvatar());
        //暂且标为发送者的昵称，可能为陌生人
        msgJson.getData().setRemark(sendUser.getNickname());
        //用来从session集合中找对应的session
        int toId = msgJson.getData().getTo_id();
        Session session = socketSessionMap.get(toId+"");
        //找到备注名
        int userId = socketService.queryUserIdByPid(toId);
        String remark = socketService.queryRemark(userId, sendUser.getId());
        if(!StringUtil.isEmpty(remark)){
            msgJson.getData().setRemark(remark);
        }

        try {
            session.getBasicRemote().sendText(gson.toJson(msgJson));
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
