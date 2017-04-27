package com.mumu.meishijia.websocket;

import lib.utils.StringUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/26.
 */
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("Before Handshake");
        String principalId = ((ServletServerHttpRequest) request).getServletRequest().getParameter("principal_id");
        if (StringUtil.isEmpty(principalId)) {
            return false;
        } else {
            attributes.put("principal_id", principalId);
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        System.out.println("After Handshake");
    }
}
