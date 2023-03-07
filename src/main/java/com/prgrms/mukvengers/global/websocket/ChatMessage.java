package com.prgrms.mukvengers.global.websocket;

public record ChatMessage(
	MessageType type,
	String content,
	String sender,
	Long userId) {

	public ChatMessage(MessageType type, String sender) {
		this(type, null, sender, 1L);
	}

	public ChatMessage(String content, String sender) {
		this(MessageType.CHAT, content, sender, 1L);
	}
}
