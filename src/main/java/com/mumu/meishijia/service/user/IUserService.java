package com.mumu.meishijia.service.user;

import com.mumu.meishijia.model.user.UserModel;
import com.mumu.meishijia.pojo.user.User;

/**
 * UserService的接口
 * Created by Administrator on 2017/3/28.
 */
public interface IUserService {
    int register(String username, String password, String verifyCode);
    UserModel login(String username, String password);
    void logout(String token);
    int updatePassword(String username, String password);
    int updateUser(User user);
    int updateAvatar(int id, String avatar);
}
