package com.mumu.meishijia.websocket;

import com.google.gson.Gson;
import com.mumu.meishijia.model.im.MsgJsonModel;
import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.service.im.ISocketService;
import lib.utils.StringUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/26.
 */
public class ChatMessageHandler extends TextWebSocketHandler{

    private String principalId;
    //用户名和webSocket的session绑定的路由表
    private static Map<String, WebSocketSession> sessionMap;
    static {
        sessionMap = new HashMap<String, WebSocketSession>();
    }

    @Resource
    private ISocketService socketService;

    public ChatMessageHandler() {}

    /**
     * 连接成功时候，会触发UI上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......");
        principalId = (String) session.getAttributes().get("principal_id");
        System.out.println("connect principal_id is "+principalId);
        sessionMap.put(principalId, session);
    }

    /**
     * 在UI在用js调用websocket.send()时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        //同样的消息再返回去，代表接收到了消息
        session.sendMessage(message);
        Gson gson = new Gson();
        MsgJsonModel msgJson = gson.fromJson(message.getPayload(), MsgJsonModel.class);
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
        int toId = msgJson.getData().getTo_id();
        //找到备注名
        int userId = socketService.queryUserIdByPid(toId);
        String remark = socketService.queryRemark(userId, sendUser.getId());
        if(!StringUtil.isEmpty(remark)){
            msgJson.getData().setRemark(remark);
        }
        //用来从session集合中找对应的session
        WebSocketSession toSession = sessionMap.get(toId+"");

        String sendMessage = gson.toJson(msgJson);
        if(toSession != null){
            toSession.sendMessage(new TextMessage(sendMessage));
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
//        for (WebSocketSession user : users) {
//            try {
//                if (user.isOpen()) {
//                    user.sendMessage(message);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            break;
//        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
//        for (WebSocketSession user : users) {
//            try {
//                if (user.isOpen()) {
//                    user.sendMessage(message);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        System.out.println("websocket connection error......");
        exception.printStackTrace();
        sessionMap.remove(principalId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("websocket connection closed......");
        System.out.println("close principal_id is"+principalId);
        sessionMap.remove(principalId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
