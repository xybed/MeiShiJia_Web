package com.mumu.meishijia.controller;

import com.google.gson.Gson;
import com.mumu.meishijia.model.im.MsgJsonModel;
import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.service.im.ISocketService;
import lib.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 测试用的controller
 * Created by Administrator on 2017/3/28.
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController{
    @Resource
    private ISocketService socketService;

    @RequestMapping("/test")
    @ResponseBody
    public String test(HttpServletRequest request){
        String message = "{\"conversation_id\":1001,\"data\":{\"friend_id\":0,\"from_id\":1001,\"msg_content\":\"{\\\"second\\\":0,\\\"text\\\":\\\"456\\\"}\",\"system_attach\":0,\"time\":1493781987915,\"to_id\":1002},\"msg_id\":1493781987945,\"msg_type\":1}";
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
        int toId = msgJson.getData().getTo_id();
        //找到备注名
        int userId = socketService.queryUserIdByPid(toId);
        String remark = socketService.queryRemark(userId, sendUser.getId());
        if(!StringUtil.isEmpty(remark)){
            msgJson.getData().setRemark(remark);
        }

        String sendMessage = gson.toJson(msgJson);
        return sendMessage;
    }
}
