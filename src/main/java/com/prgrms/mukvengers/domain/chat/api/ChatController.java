package com.prgrms.mukvengers.domain.chat.api;

import static org.springframework.http.MediaType.*;

import java.util.List;
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
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponses;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.domain.chat.service.ChatService;
import com.prgrms.mukvengers.global.base.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@ResponseBody
	@GetMapping(value = "/api/v2/crews/{crewId}/chats", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<ChatsInCrew>> getChattingList(
		@PathVariable(name = "crewId") Long crewId,
		@PageableDefault(sort = "createdAt") Pageable pageable) {

		ChatsInCrew result = chatService.getByCrewId(crewId, pageable);

		return ResponseEntity.ok(new ApiResponse<>(result));
	}

	@ResponseBody
	@GetMapping(value = "/api/v1/crews/{crewId}/chats", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<ChatResponses>> getChattingList(
		@PathVariable(name = "crewId") Long crewId) {

		List<ChatResponse> result = chatService.getAllByCrewId(crewId);

		ChatResponses chatResponses = new ChatResponses(result);

		return ResponseEntity.ok(new ApiResponse<>(chatResponses));
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