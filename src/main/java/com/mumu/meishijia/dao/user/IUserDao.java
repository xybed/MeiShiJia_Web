package com.mumu.meishijia.dao.user;

import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.pojo.user.UserToken;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/3/28.
 */
@Repository("userDao")
public interface IUserDao {
    int register(User user);
    int updatePid(String username);
    User login(User user);
    void insertToken(UserToken userToken);
}
