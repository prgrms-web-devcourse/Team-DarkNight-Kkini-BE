package com.prgrms.mukvengers.domain.chat.service;

import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.domain.chat.model.ChatMessage;

public interface ChatService {
	void save(ChatMessage chatMessage, Long crewId);

	ChatsInCrew getByCrewId(Long crewId, Pageable pageable);
}
