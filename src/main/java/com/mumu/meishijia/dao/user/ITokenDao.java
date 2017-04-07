package com.mumu.meishijia.dao.user;

import com.mumu.meishijia.pojo.user.UserToken;
import org.springframework.stereotype.Repository;

/**
 * token的dao
 * Created by Administrator on 2017/4/7.
 */
@Repository("tokenDao")
public interface ITokenDao {
    UserToken queryToken(String token);
}
