package com.prgrms.mukvengers.domain.chat.service;

import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.global.websocket.ChatMessage;

public interface ChatService {
	IdResponse save(ChatMessage chatMessage, Long crewId);

	ChatsInCrew getByCrewId(Long crewId, Pageable pageable);
}
