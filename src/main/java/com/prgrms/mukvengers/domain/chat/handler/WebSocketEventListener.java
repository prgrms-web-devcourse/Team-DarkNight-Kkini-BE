package com.prgrms.mukvengers.domain.chat.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.prgrms.mukvengers.domain.chat.model.ChatMessage;
import com.prgrms.mukvengers.domain.chat.model.MessageType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	private final SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.info("Received a new web socket connection");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String)headerAccessor.getSessionAttributes().get("username");
		Long userId = (Long)headerAccessor.getSessionAttributes().get("userId");
		Long crewId = (Long)headerAccessor.getSessionAttributes().get("crewId");

		if (username != null && userId != null && crewId != null) {
			logger.info("User Disconnected : " + username);

			ChatMessage chatMessage = new ChatMessage(MessageType.LEAVE, username, userId);

			messagingTemplate.convertAndSend("/topic/public/" + crewId, chatMessage);
		}
	}
}