package com.prgrms.mukvengers.global.websocket;

import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.global.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		log.error("message:" + message);
		log.error("헤더 : " + message.getHeaders());
		log.error("토큰" + accessor.getNativeHeader("Authorization"));
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			jwtTokenProvider.validateToken(
				extractTokenByHeader(accessor.getFirstNativeHeader("Authorization")));
		}
		return message;
	}

	private String extractTokenByHeader(String authHeaderValue) {

		if (Objects.nonNull(authHeaderValue) &&
			authHeaderValue.toLowerCase().startsWith("Bearer" .toLowerCase())) {
			return authHeaderValue.substring("Bearer" .length()).trim();
		}

		throw new IllegalArgumentException("헤더에 값이 없습니다");
	}
}
