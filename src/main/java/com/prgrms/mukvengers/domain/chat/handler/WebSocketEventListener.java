package com.prgrms.mukvengers.domain.chat.handler;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.request.MessageType;
import com.prgrms.mukvengers.domain.chat.exception.WebSocketException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	private final SimpMessageSendingOperations messagingTemplate;

	// 연결 요청
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.info("Received a new web socket connection");
	}

	// 구독 요청(입장)
	@EventListener
	public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
		logger.info("Received a new web socket subscribe");
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String)getValue(accessor, "username");
		Long userId = (Long)getValue(accessor, "userId");
		Long crewId = (Long)getValue(accessor, "crewId");

		logger.info("User: {} {} Subscribe Crew : {}", userId, username, crewId);
		ChatRequest chatRequest = new ChatRequest(MessageType.JOIN, userId,
			username + " 님이 입장했습니다.");
		messagingTemplate.convertAndSend("/topic/public/" + crewId, chatRequest);

	}

	// 연결 해제
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String)getValue(accessor, "username");
		Long userId = (Long)getValue(accessor, "userId");
		Long crewId = (Long)getValue(accessor, "crewId");

		logger.info("User: {} {} Disconnected Crew : {}", userId, username, crewId);

		ChatRequest chatRequest = new ChatRequest(
			MessageType.LEAVE, userId, username + " 님이 떠났습니다.");

		messagingTemplate.convertAndSend("/topic/public/" + crewId, chatRequest);
	}

	private Object getValue(StompHeaderAccessor accessor, String key) {
		Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
		Object value = sessionAttributes.get(key);

		if (Objects.isNull(value)) {
			throw new WebSocketException(key + " 에 해당하는 값이 없습니다.");
		}

		return value;
	}

	private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (Objects.isNull(sessionAttributes)) {
			throw new WebSocketException("SessionAttributes가 null입니다.");
		}
		return sessionAttributes;
	}
}