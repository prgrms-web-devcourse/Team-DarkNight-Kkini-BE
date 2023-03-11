package com.prgrms.mukvengers.domain.chat.api;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.domain.chat.service.ChatService;
import com.prgrms.mukvengers.global.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	@ResponseBody
	@GetMapping("/api/v1/crews/{crewId}/chats")
	public ResponseEntity<ApiResponse<ChatsInCrew>> getChattingList(
		@PathVariable(name = "crewId") Long crewId,
		@PageableDefault(sort = "createdAt") Pageable pageable) {

		ChatsInCrew result = chatService.getByCrewId(crewId, pageable);

		return ResponseEntity.ok(new ApiResponse<>(result));
	}

	@MessageMapping("/chat.sendMessage/{crewId}")
	@SendTo("/topic/public/{crewId}")
	public ChatResponse sendMessage(@DestinationVariable Long crewId,
		@Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
		@Payload ChatRequest chatRequest
	) {
		return chatService.save(chatRequest, crewId, simpSessionAttributes);
	}
}