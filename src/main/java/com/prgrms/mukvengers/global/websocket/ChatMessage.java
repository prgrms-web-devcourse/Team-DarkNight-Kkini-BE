package com.prgrms.mukvengers.global.websocket;

public record ChatMessage(
	MessageType type,
	String content,
	String sender) {

	public ChatMessage(MessageType type, String sender) {
		this(type, null, sender);
	}
}
