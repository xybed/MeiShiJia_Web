package com.mumu.meishijia.service.user;

import com.mumu.meishijia.dao.user.ITokenDao;
import com.mumu.meishijia.pojo.user.UserToken;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * token的service
 * Created by Administrator on 2017/4/7.
 */
@Service("tokenService")
public class TokenService implements ITokenService{

    @Resource
    private ITokenDao tokenDao;

    public UserToken queryToken(String token){
        return tokenDao.queryToken(token);
    }
}
