package com.prgrms.mukvengers.global.websocket.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.domain.chat.service.ChatService;
import com.prgrms.mukvengers.global.common.dto.ApiResponse;
import com.prgrms.mukvengers.global.websocket.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//TODO 퇴장 처리, username 처리,
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	/**
	 * 웹소켓 테스트용 임시 Endpoint
	 */
	@GetMapping("/")
	public String home() {
		return "index";
	}

	/**
	 * 웹소켓 테스트용 임시 Endpoint
	 */
	@ResponseBody
	@GetMapping("/chat/{crewId}")
	public ResponseEntity<ApiResponse<ChatsInCrew>> getChattingList(
		@PathVariable(name = "crewId") Long crewId,
		@PageableDefault(sort = "createdAt") Pageable pageable) {

		ChatsInCrew result = chatService.getByCrewId(crewId, pageable);

		return ResponseEntity.ok(new ApiResponse<>(result));
	}

	@MessageMapping("/chat.sendMessage/{crewId}")
	@SendTo("/topic/public/{crewId}")
	public ChatMessage sendMessage(@DestinationVariable Long crewId,
		@Payload ChatMessage chatMessage
	) {
		chatService.save(chatMessage, crewId);

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