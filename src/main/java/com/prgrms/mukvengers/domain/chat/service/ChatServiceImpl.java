package com.prgrms.mukvengers.domain.chat.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.domain.chat.mapper.ChatMapper;
import com.prgrms.mukvengers.domain.chat.model.Chat;
import com.prgrms.mukvengers.domain.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final ChatRepository chatRepository;
	private final ChatMapper chatMapper;

	@Override
	@Transactional
	public ChatResponse save(ChatRequest chatRequest, Long crewId, Map<String, Object> header) {
		Chat chat = chatMapper.toChat(chatRequest, crewId);
		Chat savedChat = chatRepository.save(chat);

		return toChatResponse(savedChat, header);
	}

	@Override
	public ChatsInCrew getByCrewId(Long crewId, Pageable pageable) {
		Page<ChatResponse> result = chatRepository.findByCrewId(crewId, pageable);
		return new ChatsInCrew(result);
	}

	private ChatResponse toChatResponse(Chat chat, Map<String, Object> header) {
		String username = getValueFromHeader(header, "username");
		String profileImgUrl = getValueFromHeader(header, "profileImgUrl");

		return chatMapper.toChatResponse(chat, username, profileImgUrl);
	}

	private String getValueFromHeader(Map<String, Object> header, String key) {
		return (String)header.get(key);
	}
}
