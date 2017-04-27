package com.mumu.meishijia.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by Administrator on 2017/4/26.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(chatMessageHandler(),"/chatServer")
                .setAllowedOrigins("*").addInterceptors(new ChatHandshakeInterceptor());
    }

    @Bean
    public TextWebSocketHandler chatMessageHandler(){
        return new ChatMessageHandler();
    }
}
