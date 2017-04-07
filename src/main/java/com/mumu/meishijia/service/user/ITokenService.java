package com.mumu.meishijia.service.user;

import com.mumu.meishijia.pojo.user.UserToken;

/**
 * tokenService的接口
 * Created by Administrator on 2017/4/7.
 */
public interface ITokenService {
    UserToken queryToken(String token);
}
