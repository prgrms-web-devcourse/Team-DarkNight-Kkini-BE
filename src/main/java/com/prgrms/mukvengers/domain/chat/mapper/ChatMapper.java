package com.prgrms.mukvengers.domain.chat.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.model.Chat;
import com.prgrms.mukvengers.domain.chat.model.ChatMessage;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface ChatMapper {

	@Mapping(target = "content", source = "chatMessage.content")
	@Mapping(target = "sender", source = "chatMessage.sender")
	@Mapping(target = "userId", source = "chatMessage.userId")
	@Mapping(target = "crewId", source = "crewId")
	Chat toChat(ChatMessage chatMessage, Long crewId);

	@Mapping(target = "content", source = "content")
	@Mapping(target = "sender", source = "sender")
	@Mapping(target = "createdAt", source = "createdAt")
	ChatResponse toChatResponse(Chat chat);
}
