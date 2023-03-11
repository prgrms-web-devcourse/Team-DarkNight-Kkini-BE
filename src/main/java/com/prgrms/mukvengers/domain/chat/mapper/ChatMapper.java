package com.prgrms.mukvengers.domain.chat.mapper;

import static org.mapstruct.ReportingPolicy.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.model.Chat;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface ChatMapper {

	@Mapping(target = "content", source = "chatRequest.content")
	@Mapping(target = "userId", source = "chatRequest.userId")
	@Mapping(target = "type", source = "chatRequest.type")
	@Mapping(target = "crewId", source = "crewId")
	Chat toChat(ChatRequest chatRequest, Long crewId);

	@Mapping(target = "type", source = "chat.type")
	@Mapping(target = "userId", source = "chat.userId")
	@Mapping(target = "username", source = "username")
	@Mapping(target = "profileImgUrl", source = "profileImgUrl")
	@Mapping(target = "createdAt", source = "chat.createdAt")
	@Mapping(target = "content", source = "chat.content")
	ChatResponse toChatResponse(Chat chat, String username, String profileImgUrl);
}
