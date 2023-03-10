package com.prgrms.mukvengers.domain.chat.model;

public record ChatMessage(
	MessageType type,
	String content,
	String sender,
	Long userId) {

	public ChatMessage(MessageType type, String sender, Long userId) {
		this(type, null, sender, userId);
	}

	public ChatMessage(String content, String sender, Long userId) {
		this(MessageType.CHAT, content, sender, userId);
	}
}
