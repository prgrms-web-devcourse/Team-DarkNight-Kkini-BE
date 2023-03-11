package com.prgrms.mukvengers.domain.chat.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.prgrms.mukvengers.base.ServiceTest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.model.Chat;
import com.prgrms.mukvengers.utils.ChatObjectProvider;

class ChatServiceImplTest extends ServiceTest {

	private Long userId;

	private Long crewId;

	@BeforeEach
	void setup() {
		userId = 100L;
		crewId = 100L;
	}

	@Test
	void save() {
		Chat chat = ChatObjectProvider.getChat(crewId, userId, "테스트 저장");
		Chat savedOne = chatRepository.save(chat);

		assertEquals(savedOne.getId(), chat.getId());
		assertEquals(savedOne.getCrewId(), chat.getCrewId());
		assertEquals(savedOne.getUserId(), chat.getUserId());
		assertEquals(savedOne.getContent(), chat.getContent());
		assertEquals(savedOne.getType(), chat.getType());
	}

	@Test
	void getByCrewId() {
		//given
		Sort sort = Sort.by(Sort.Order.desc("createdAt"));
		List<Chat> chatList = ChatObjectProvider.getChatList(crewId, userId);

		chatRepository.saveAll(chatList);

		//when
		PageRequest pageRequest = PageRequest.of(0, 5, sort);
		Page<ChatResponse> result = chatRepository.findByCrewId(crewId, pageRequest);

		//then
		assertEquals(5, result.getSize());
	}
}