package com.mumu.meishijia.dao.user;

import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.pojo.user.UserToken;
import org.springframework.stereotype.Repository;

/**
 * 有关用户的dao
 * Created by Administrator on 2017/3/28.
 */
@Repository("userDao")
public interface IUserDao {
    int verifyRegister(String username);
    int verifyPassword(String username, String password);
    int register(User user);
    int updatePid(String username);
    User login(User user);
    void logout(String token);
    void insertOrUpdateToken(UserToken userToken);
    int updatePassword(String username, String password);
    int updateUser(User user);
}
