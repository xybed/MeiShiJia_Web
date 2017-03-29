package com.mumu.meishijia.service.user;

import com.mumu.meishijia.constacts.Constacts;
import com.mumu.meishijia.dao.user.IUserDao;
import com.mumu.meishijia.model.user.UserModel;
import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.pojo.user.UserToken;
import lib.utils.DateUtil;
import lib.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 有关用户的service
 * Created by Administrator on 2017/3/28.
 */
@Service("userService")
public class UserService implements IUserService{
    @Resource
    private IUserDao userDao;

    @Transactional
    public int register(String username, String password, String verifyCode) {
        /*
        1.先检测此用户是否注册过
        2.若注册过，密码是否相同
        3.没有注册过就注册，设置默认信息
        4.设置消息主体id，3、4步骤要用到事务
         */
        int isRegister = userDao.verifyRegister(username);
        if(isRegister == 1){
            int passwordIsCorrect = userDao.verifyPassword(username, password);
            if(passwordIsCorrect == 1){
                return 3;
            }else {
                return 2;
            }
        }

        User user = new User();
        String registerDate = DateUtil.getTime("yyyy-MM-dd HH:mm:ss");

        user.setUsername(username);
        user.setPassword(password);
        user.setVerifyCode(verifyCode);
        user.setRegisterDate(registerDate);
        user.setAvatar("avatar/icon_default_avatar.png");
        //注册时，昵称用手机号代替
        user.setNickname(username);
        user.setMobilePhone(username);
        //性别为未知
        user.setSex(0);

        try {
            //注册用户
            int affectedCount1 = userDao.register(user);
            //如果sql执行不成功，affectedCount1则会为0，这一句便会抛异常，事务回滚
            int i = 1 / affectedCount1;
            //设置pid
            int affectedCount2 = userDao.updatePid(username);
            i = 1 / affectedCount2;
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    public UserModel login(String username, String password){
        /*
        1.登录验证密码是否正确
        2.生成token
         */
        User user = new User();
        UserModel userModel = new UserModel();
        user.setUsername(username);
        user.setPassword(password);

        user = userDao.login(user);
        if(user != null){
            long time = System.currentTimeMillis();
            String token = MD5Util.MD5(username + time);
            long deadline = time + 7 * 24 * 60 * 60 * 1000;
            UserToken userToken = new UserToken();
            userToken.setUsername(username);
            userToken.setToken(token);
            userToken.setDeadline(deadline+"");
            userDao.insertOrUpdateToken(userToken);

            userModel.setId(user.getId());
            userModel.setUsername(user.getUsername());
            userModel.setAvatar(Constacts.BaseUrl + user.getAvatar());
            userModel.setRealName(user.getRealName());
            userModel.setNickname(user.getNickname());
            userModel.setMobilePhone(user.getMobilePhone());
            userModel.setSex(user.getSex());
            userModel.setBirthday(user.getBirthday());
            userModel.setEmail(user.getEmail());
            userModel.setCity(user.getCity());
            userModel.setAddress(user.getAddress());
            userModel.setPrinciple_id(user.getPrinciple_id());
            userModel.setToken(token);
        }else {
            //没有此用户
            return null;
        }
        return userModel;
    }
}
