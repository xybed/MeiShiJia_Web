package com.mumu.meishijia.controller.user;

import com.mumu.meishijia.constacts.Constants;
import com.mumu.meishijia.controller.BaseController;
import com.mumu.meishijia.model.BaseModel;
import com.mumu.meishijia.model.user.UserModel;
import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.service.user.IUserService;
import lib.utils.FileUtil;
import lib.utils.MD5Util;
import lib.utils.NumberUtil;
import lib.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
        //0注册失败(sql问题)、1注册成功、2注册过、3且密码相同
        int result = userService.register(username, password, verifyCode);
        switch (result){
            case 0:
                baseModel.setResultType(-1);
                baseModel.setResultCode(-1);
                baseModel.setDetail("注册失败，请稍后再试");
                break;
            case 1:
                baseModel.setResultType(0);
                baseModel.setResultCode(0);
                baseModel.setDetail("注册成功");
                baseModel.setData("注册成功");
                break;
            case 2:
                baseModel.setResultType(-1);
                baseModel.setResultCode(-1);
                baseModel.setDetail("该用户已注册过");
                break;
            case 3:
                baseModel.setResultType(0);
                baseModel.setResultCode(0);
                baseModel.setDetail("该用户注册过且密码相同，自动为您登录");
                baseModel.setData("该用户注册过且密码相同，自动为您登录");
                break;
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

    @RequestMapping("/logout")
    @ResponseBody
    public BaseModel logout(HttpServletRequest request){
        String queryString = request.getQueryString();
        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
        if(!validateSign(queryString, sign)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            return baseModel;
        }

        String token = request.getParameter("token");
        userService.logout(token);
        baseModel.setResultType(0);
        baseModel.setResultCode(0);
        baseModel.setDetail("退出登录成功");
        return baseModel;
    }

    @RequestMapping("/modifyPwd")
    @ResponseBody
    public BaseModel modifyPassword(HttpServletRequest request){
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
        if(StringUtil.isEmpty(username) || StringUtil.isEmpty(password)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            return baseModel;
        }
        int result = userService.updatePassword(username, password);
        if(result == 0){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("没有此用户");
        }else {
            baseModel.setResultType(0);
            baseModel.setResultCode(0);
            baseModel.setDetail("修改密码成功");
            baseModel.setData("修改密码成功");
        }
        return baseModel;
    }

    @RequestMapping("/modifyUserInfo")
    @ResponseBody
    public BaseModel modifyUserInfo(HttpServletRequest request){
        String queryString = request.getQueryString();
        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
        if(!validateSign(queryString, sign)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            baseModel.setData(null);
            return baseModel;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String nickname = request.getParameter("nickname");
        String realName = request.getParameter("real_name");
        int sex = NumberUtil.parseInt(request.getParameter("sex"), 0);
        String birthday = request.getParameter("birthday");
        String email = request.getParameter("email");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String signature = request.getParameter("signature");
        User user = new User();
        user.setId(id);
        user.setNickname(nickname);
        user.setReal_name(realName);
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setEmail(email);
        user.setProvince(province);
        user.setCity(city);
        user.setSignature(signature);
        int result = userService.updateUser(user);
        if(result == 0){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("没有此用户");
        }else {
            baseModel.setResultType(0);
            baseModel.setResultCode(0);
            baseModel.setDetail("修改信息成功");
            baseModel.setData("修改信息成功");
        }
        return baseModel;
    }

    @RequestMapping("/modifyAvatar")
    @ResponseBody
    public BaseModel modifyAvatar(HttpServletRequest request, @RequestParam("img_file")MultipartFile file){
        String queryString = request.getQueryString();
        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
        if(!validateSign(queryString, sign)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            baseModel.setData(null);
            return baseModel;
        }

        String userId = request.getParameter("id");
        //存图片,名字根据id加密
        String avatar = MD5Util.MD5(userId);
        try {
            FileUtil.saveImage(file.getBytes(), getApplicationPath() + "/avatar/", avatar + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int result = userService.updateAvatar(NumberUtil.parseInt(userId, 0), "avatar/" + avatar + ".png");
        if(result == 0){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("修改头像失败");
            baseModel.setData("修改头像失败");
        }else {
            baseModel.setResultType(0);
            baseModel.setResultCode(0);
            baseModel.setDetail("修改头像成功");
            baseModel.setData(Constants.BaseUrl + "avatar/" + avatar + ".png");
        }
        return baseModel;
    }
}
