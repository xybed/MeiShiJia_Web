package com.mumu.meishijia.controller;

import com.mumu.meishijia.pojo.user.UserToken;
import com.mumu.meishijia.service.user.ITokenService;
import lib.utils.MD5Util;
import lib.utils.NumberUtil;
import lib.utils.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseController
 * Created by 7mu on 2016/11/23.
 */
public class BaseController {
    private static final String TOKEN_KEY = "MeiShiJia";
    @Resource
    private ITokenService tokenService;

    protected String getApplicationPath(){
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        return servletContext.getRealPath("/");
    }

    /**
     * 验证sign的有效性，保证请求是安全无恶意的
     * @param queryString 请求携带的参数
     * @param sign 加密的sign
     * @return 安全则返回true，反之为false
     */
    protected boolean validateSign(String queryString, String sign){
        if(StringUtil.isEmpty(sign))
            return false;
        if(StringUtil.isEmpty(queryString))
            return false;
        try {
            queryString = URLDecoder.decode(queryString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] strings =  queryString.split("&");
        Map<String, String> paramsMap = new HashMap<String, String>();
        if(strings.length > 0){
            for(String str : strings){
                if(str.contains("=") && !str.startsWith("sign=")){
                    String[] paramStrs = str.split("=");
                    if(paramStrs.length > 1){
                        paramsMap.put(paramStrs[0], paramStrs[1]);
                    }else {
                        paramsMap.put(paramStrs[0], "");
                    }
                }
            }
        }
        return sign.equals(MD5Util.createParamSign(paramsMap, TOKEN_KEY));
    }

    /**
     * 验证token的有效性
     * 1.如果表中无该token数据，证明token被清除或者token非法，则无效
     * 2.如果token期限已过，证明需要更换token，则无效
     * 3.不满足1、2条件，token为有效
     * @param token token
     * @return 有效返回true，无效返回false
     */
    public boolean validateToken(String token){
        UserToken userToken = tokenService.queryToken(token);
        return userToken != null && System.currentTimeMillis() <= NumberUtil.parseLong(userToken.getDeadline(), 0);
    }
}
