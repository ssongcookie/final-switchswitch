package com.kh.switchswitch.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.kh.switchswitch.common.socket.handler.AlarmHandler;
import com.kh.switchswitch.common.socket.handler.ChattingHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer  {
	
	private final ChattingHandler chattingHandler;
	private final AlarmHandler alarmHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		 registry.addHandler(chattingHandler, "/chat")
		 	.addHandler(alarmHandler, "/alarm")
		 	.addInterceptors(new HttpSessionHandshakeInterceptor())
		 	.setAllowedOrigins("http://localhost:9090")
		 	.setAllowedOrigins("http://localhost:9898")
		 	.withSockJS();
	}

}
