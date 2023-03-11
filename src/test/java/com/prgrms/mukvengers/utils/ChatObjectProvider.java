package com.prgrms.mukvengers.utils;

import java.util.List;
import java.util.stream.IntStream;

import com.prgrms.mukvengers.domain.chat.dto.request.MessageType;
import com.prgrms.mukvengers.domain.chat.model.Chat;

public class ChatObjectProvider {

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
}
