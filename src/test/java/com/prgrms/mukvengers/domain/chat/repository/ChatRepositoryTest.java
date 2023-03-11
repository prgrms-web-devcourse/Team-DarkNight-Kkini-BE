package com.prgrms.mukvengers.domain.chat.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.utils.ChatObjectProvider;

class ChatRepositoryTest extends RepositoryTest {

	@Autowired
	private ChatRepository chatRepository;

	@Test
	void findByCrewId_Test() {
		Long crewId = 1L;
		Long userId = 1L;
		chatRepository.saveAll(ChatObjectProvider.getChatList(crewId, userId));
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
		Page<ChatResponse> chatResponses = chatRepository.findByCrewId(crewId, pageable);

		assertThat(chatResponses).isNotEmpty();
		assertThat(chatResponses.stream().toList().get(0).content()).isEqualTo("content0");
	}
}