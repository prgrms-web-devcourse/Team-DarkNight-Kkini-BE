package com.prgrms.mukvengers.global.config.web.socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.prgrms.mukvengers.domain.chat.handler.StompHandler;

import lombok.RequiredArgsConstructor;

@EnableWebSocketMessageBroker
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final StompHandler stompHandler;

	@Value("${front.server}")
	private String frontServer;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			.setAllowedOrigins("http://localhost:3000", frontServer)
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}

}