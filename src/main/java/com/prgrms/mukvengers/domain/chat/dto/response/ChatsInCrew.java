package com.prgrms.mukvengers.domain.chat.dto.response;

import org.springframework.data.domain.Page;

public record ChatsInCrew(
	Page<ChatResponse> chats
) {
}
