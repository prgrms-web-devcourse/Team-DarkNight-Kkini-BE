package com.prgrms.mukvengers.global.websocket.api;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.prgrms.mukvengers.global.websocket.ChatMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ChatController {
	/**
	 * 웹소켓 테스트용 임시 Endpoint
	 */
	@GetMapping("/")
	public String home() {
		return "index";
	}

	@MessageMapping("/chat.sendMessage/{crewId}")
	@SendTo("/topic/public/{crewId}")
	public ChatMessage sendMessage(@DestinationVariable String crewId, @Payload ChatMessage chatMessage) {
		return chatMessage;
	}

	@MessageMapping("/chat.addUser/{crewId}")
	@SendTo("/topic/public/{crewId}")
	public ChatMessage addUser(@DestinationVariable String crewId, @Payload ChatMessage chatMessage,
		SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.sender());
		return chatMessage;
	}
}