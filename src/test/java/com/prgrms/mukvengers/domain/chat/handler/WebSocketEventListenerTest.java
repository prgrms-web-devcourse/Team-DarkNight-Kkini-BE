package com.prgrms.mukvengers.domain.chat.handler;

import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.prgrms.mukvengers.base.ControllerTest;
import com.prgrms.mukvengers.domain.chat.dto.request.ChatRequest;
import com.prgrms.mukvengers.domain.chat.dto.request.MessageType;

@SpringBootTest
class WebSocketEventListenerTest extends ControllerTest {

	private static final String MESSAGE = "testMessage";

	private final Map<String, Object> header = new HashMap<>();
	private final Map<String, Object> sessionAttributes = new HashMap<>();
	private final Map<String, List<String>> nativeHeaders = new HashMap<>();

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@MockBean
	private SimpMessagingTemplate simpMessagingTemplate;

	private SessionConnectedEvent sessionConnectedEvent;
	private SessionSubscribeEvent sessionSubscribeEvent;
	private SessionDisconnectEvent sessionDisconnectEvent;

	@Test
	@DisplayName("웹소켓 연결 시 이벤트 발생")
	void handleWebSocketConnectListenerTest() {
		// Given
		sessionAttributes.put("userId", savedUser1.getId());
		nativeHeaders.put(HttpHeaders.AUTHORIZATION, List.of("Bearer " + accessToken1));

		// When
		sessionConnectedEvent = new SessionConnectedEvent(this, setTestMessage(StompCommand.CONNECTED));
		eventPublisher.publishEvent(sessionConnectedEvent);

		// Then
		then(simpMessagingTemplate).shouldHaveNoInteractions();
	}

	@Test
	@DisplayName("웹소켓 구독 시 이벤트 발생")
	void handleWebSocketSubscribeListenerTest() {
		// Given
		Long crewId = 123L;
		String defaultPath = "/topic/public/";
		ChatRequest chatRequest = new ChatRequest(MessageType.JOIN, savedUser1.getId(),
			savedUser1.getNickname() + " 님이 입장했습니다.");

		sessionAttributes.put("userId", savedUser1.getId());
		sessionAttributes.put("username", savedUser1.getNickname());
		sessionAttributes.put("crewId", 123L);

		// When
		sessionSubscribeEvent = new SessionSubscribeEvent(this, setTestMessage(StompCommand.SUBSCRIBE));
		eventPublisher.publishEvent(sessionSubscribeEvent);

		// Then
		then(simpMessagingTemplate).should().convertAndSend(defaultPath + crewId, chatRequest);
	}

	@Test
	@DisplayName("웹소켓 연결 해제 시 이벤트 발생")
	void handleWebSocketDisconnectListenerTest() {
		// Given
		Long crewId = 123L;
		String defaultPath = "/topic/public/";
		ChatRequest chatRequest = new ChatRequest(
			MessageType.LEAVE, savedUser1.getId(), savedUser1.getNickname() + " 님이 떠났습니다.");

		sessionAttributes.put("userId", savedUser1.getId());
		sessionAttributes.put("username", savedUser1.getNickname());
		sessionAttributes.put("crewId", 123L);

		// When
		sessionDisconnectEvent = new SessionDisconnectEvent(this, setTestMessage(StompCommand.DISCONNECT),
			"testSessionId", CloseStatus.NORMAL);
		eventPublisher.publishEvent(sessionDisconnectEvent);

		// Then
		then(simpMessagingTemplate).should().convertAndSend(defaultPath + crewId, chatRequest);
	}

	private Message<byte[]> setTestMessage(StompCommand command) {

		header.put("simpSessionAttributes", sessionAttributes);
		header.put("simpSessionId", "testSessionId"); //
		header.put("nativeHeaders", nativeHeaders);
		header.put("stompCommand", command);

		MessageHeaders messageHeaders = new MessageHeaders(header);

		return MessageBuilder.createMessage(MESSAGE.getBytes(StandardCharsets.UTF_8), messageHeaders);
	}
}
