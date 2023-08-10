package com.prgrms.mukvengers.domain.chat.repository;

import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.StoreObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.prgrms.mukvengers.base.RepositoryTest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.utils.ChatObjectProvider;

class ChatRepositoryTest extends RepositoryTest {

	@Autowired
	private ChatRepository chatRepository;

	User savedUserForChat;
	Store savedStoreForChat;
	Crew crewForChat;

	@BeforeEach
	void setUpChat() {
		savedUserForChat = userRepository.save(createUser("testUser"));
		savedStoreForChat = storeRepository.save(createStore("121212"));
		crewForChat = crewRepository.save(createCrew(savedStore));
	}

	@Test
	@DisplayName("[성공] 채팅방에 채팅한 내용을 불러올 수 있다.")
	void findByCrewId_Test() {
		// given
		Long crewId = crewForChat.getId();
		Long userId = savedUserForChat.getId();
		chatRepository.saveAll(ChatObjectProvider.getChatList(crewId, userId));
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));

		//when
		Page<ChatResponse> chatResponses = chatRepository.findByCrewId(crewId, pageable);

		//then
		assertThat(chatResponses).isNotEmpty();
		assertThat(chatResponses.stream().toList().get(0).content()).isEqualTo("content0");
	}
}