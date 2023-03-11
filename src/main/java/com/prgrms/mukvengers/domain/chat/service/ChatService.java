package com.prgrms.mukvengers.domain.chat.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;

public interface ChatService {
	ChatResponse save(ChatRequest chatRequest, Long crewId, Map<String, Object> headers);

	ChatsInCrew getByCrewId(Long crewId, Pageable pageable);
}
