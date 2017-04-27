package com.mumu.meishijia.controller;

import com.google.gson.Gson;
import com.mumu.meishijia.model.im.MsgJsonModel;
import com.mumu.meishijia.service.im.ISocketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public int test(HttpServletRequest request){
        String s = request.getParameter("test");
        //存消息记录到数据库
        int msgId = socketService.insertMessage(null);
        return msgId;
    }
}
