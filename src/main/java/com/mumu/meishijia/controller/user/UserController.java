package com.mumu.meishijia.controller.user;

import com.mumu.meishijia.controller.BaseController;
import com.mumu.meishijia.model.BaseModel;
import com.mumu.meishijia.model.user.UserModel;
import com.mumu.meishijia.service.user.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 关于用户的controller
 * Created by Administrator on 2017/3/28.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
    @Resource
    private IUserService userService;

    @RequestMapping("/register")
    @ResponseBody
    public BaseModel register(HttpServletRequest request){
        String queryString = request.getQueryString();
        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
        if(!validateSign(queryString, sign)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            return baseModel;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String verifyCode = request.getParameter("verify_code");
        int result = userService.register(username, password, verifyCode);
        if(result != 0){
            baseModel.setResultType(0);
            baseModel.setResultCode(0);
            baseModel.setDetail("注册成功");
        }else {
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("注册失败，请稍后再试");
        }
        return baseModel;
    }

    @RequestMapping("/login")
    @ResponseBody
    public BaseModel login(HttpServletRequest request){
        String queryString = request.getQueryString();
        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
        if(!validateSign(queryString, sign)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            return baseModel;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserModel userModel = userService.login(username, password);
        if(userModel != null){
            baseModel.setResultType(0);
            baseModel.setResultCode(0);
            baseModel.setDetail("登录成功");
            baseModel.setData(userModel);
        }else {
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("用户名或密码不正确");
        }
        return baseModel;
    }
}
