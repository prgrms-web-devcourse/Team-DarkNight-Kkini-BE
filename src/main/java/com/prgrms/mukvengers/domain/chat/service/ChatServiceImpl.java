package com.prgrms.mukvengers.domain.chat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.domain.chat.mapper.ChatMapper;
import com.prgrms.mukvengers.domain.chat.model.Chat;
import com.prgrms.mukvengers.domain.chat.repository.ChatRepository;
import com.prgrms.mukvengers.domain.crewmember.exception.MemberNotFoundException;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.global.common.dto.IdResponse;
import com.prgrms.mukvengers.global.websocket.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final CrewMemberRepository crewMemberRepository;
	private final ChatRepository chatRepository;
	private final ChatMapper chatMapper;

	@Override
	@Transactional
	public IdResponse save(ChatMessage chatMessage, Long crewId) {
		Chat chat = chatMapper.toChat(chatMessage, crewId);

		Long userId = chatMessage.userId();

		crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId)
			.orElseThrow(() -> new MemberNotFoundException(userId));

		chatRepository.save(chat);

		return new IdResponse(chat.getId());
	}

	@Override
	public ChatsInCrew getByCrewId(Long crewId, Pageable pageable) {
		Page<ChatResponse> result = chatRepository.findByCrewId(crewId, pageable)
			.map(chatMapper::toChatResponse);

		return new ChatsInCrew(result);
	}
}
