package com.prgrms.mukvengers.utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.request.MessageType;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponses;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatsInCrew;
import com.prgrms.mukvengers.domain.chat.model.Chat;
import com.prgrms.mukvengers.domain.user.model.User;

public class ChatObjectProvider {

	public static final String TEST_MESSAGE = "testMessage";

	public static Chat getChat(Long crewId, Long userId, String content) {
		return Chat.builder()
			.crewId(crewId)
			.userId(userId)
			.content(content)
			.type(MessageType.CHAT)
			.build();
	}

	public static List<Chat> getChatList(Long crewId, Long userId) {

		return IntStream.range(0, 10)
			.mapToObj(i -> Chat.builder()
				.userId(userId)
				.crewId(crewId)
				.content("content" + i)
				.type(MessageType.CHAT)
				.build()).toList();
	}

	public static ChatsInCrew createChatsInCrew(ChatResponse chatResponse) {
		Page<ChatResponse> chatResponseInPage = new PageImpl<>(List.of(chatResponse), Pageable.unpaged(), 1);
		return new ChatsInCrew(chatResponseInPage);
	}

	public static ChatsInCrew createChatsInCrew(ChatResponses chatResponses) {
		Page<ChatResponse> chatResponseInPage = new PageImpl<>(chatResponses.chats(), Pageable.unpaged(), 1);
		return new ChatsInCrew(chatResponseInPage);
	}

	public static ChatsInCrew createChatsInCrew(List<ChatResponse> chatResponses) {
		Page<ChatResponse> chatResponseInPage = new PageImpl<>(chatResponses, Pageable.unpaged(), 1);
		return new ChatsInCrew(chatResponseInPage);
	}

	public static ChatResponses createChatResponses() {
		return new ChatResponses(createChatResponseList());
	}

	public static List<ChatResponse> createChatResponseList() {
		return List.of(createChatResponse());
	}

	public static ChatResponse createChatResponse(User user) {
		return new ChatResponse(1L, user.getId(), user.getNickname(), user.getProfileImgUrl(),
			MessageType.CHAT, LocalDateTime.now(), TEST_MESSAGE);
	}

	public static ChatResponse createChatResponse() {
		return new ChatResponse(1L, 1L, "test", "https://example.com/testImage.jpg",
			MessageType.CHAT, LocalDateTime.now(), TEST_MESSAGE);
	}

	public static ChatRequest createChatRequest() {
		return createChatRequest(1L);
	}

	public static ChatRequest createChatRequest(Long userId) {
		return new ChatRequest(MessageType.CHAT, userId, TEST_MESSAGE);
	}
}
