package com.prgrms.mukvengers.domain.chat.handler;

import static com.prgrms.mukvengers.domain.chat.handler.StompHandler.*;
import static com.prgrms.mukvengers.utils.CrewMemberObjectProvider.*;
import static com.prgrms.mukvengers.utils.CrewObjectProvider.*;
import static com.prgrms.mukvengers.utils.StoreObjectProvider.*;
import static com.prgrms.mukvengers.utils.UserObjectProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;
import com.prgrms.mukvengers.domain.crewmember.model.vo.CrewMemberRole;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.store.model.Store;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.auth.token.service.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

class StompHandlerTest extends SliceTest {

	private static final String MESSAGE = "testMessage";
	private static final String ACCESS_TOKEN = "accessToken";

	private final Map<String, Object> header = new HashMap<>();
	private final Map<String, Object> sessionAttributes = new HashMap<>();
	private final Map<String, List<String>> nativeHeaders = new HashMap<>();

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CrewMemberRepository crewMemberRepository;

	@InjectMocks
	private StompHandler stompHandler;

	@Test
	@DisplayName("[성공] CONNECT 메시지를 보낼 때, access 토큰이 유효하면 user 정보를 세션 맵에 저장한다.")
	void preSend_connect_test_success() {
		// Given
		User user = createUser();

		Map<String, Long> userInfo = new HashMap<>();
		userInfo.put("userId", user.getId());
		Claims claims = new DefaultClaims(userInfo);

		sessionAttributes.put("userId", user.getId());
		nativeHeaders.put(HttpHeaders.AUTHORIZATION, List.of("Bearer " + ACCESS_TOKEN));

		willDoNothing().given(jwtTokenProvider).validateToken(ACCESS_TOKEN);
		given(jwtTokenProvider.getClaims(ACCESS_TOKEN)).willReturn(claims);
		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

		// When
		Message<?> sendMessage = setTestMessage(StompCommand.CONNECT);
		Message<?> resultMessage = stompHandler.preSend(sendMessage, null);

		// Then
		StompHeaderAccessor resultHeaderAccessor = StompHeaderAccessor.wrap(resultMessage);
		assertThat(resultHeaderAccessor.getCommand()).isEqualTo(StompCommand.CONNECT);
		assertThat(resultHeaderAccessor.getSessionAttributes()).isNotNull();
		assertThat(resultHeaderAccessor.getSessionAttributes())
			.containsEntry("userId", user.getId())
			.containsEntry("username", user.getNickname())
			.containsEntry("profileImgUrl", user.getProfileImgUrl());
	}

	@Test
	@DisplayName("[성공] SUBSCRIBE 메시지를 보낼 때, 목적지를 통해 Crew 정보를 검증하고 crewId 정보를 세션 맵에 저장한다.")
	void preSend_subscribe_test_success() {
		// Given
		Store store = createStore();
		Crew crew = createCrew(store);
		CrewMember crewMember = createCrewMember(1L, crew, CrewMemberRole.LEADER);
		Long crewId = 123L;

		sessionAttributes.put("userId", 1L);
		header.put("simpDestination", DEFAULT_PATH + crewId);

		given(crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, 1L))
			.willReturn(Optional.of(crewMember));

		// When
		Message<?> sendMessage = setTestMessage(StompCommand.SUBSCRIBE);
		Message<?> resultMessage = stompHandler.preSend(sendMessage, null);

		// Then
		StompHeaderAccessor resultHeaderAccessor = StompHeaderAccessor.wrap(resultMessage);
		assertThat(resultHeaderAccessor.getCommand()).isEqualTo(StompCommand.SUBSCRIBE);
		assertThat(resultHeaderAccessor.getSessionAttributes()).isNotNull();
		assertThat(resultHeaderAccessor.getSessionAttributes().get("crewId")).isNotNull();
	}

	@Test
	@DisplayName("[성공] DISCONNECT 메시지를 보낼 때, user 정보를 로그로 출력한다,")
	void preSend_disconnect_test_success() {
		// Given
		sessionAttributes.put("userId", 1L);

		// When
		Message<?> sendMessage = setTestMessage(StompCommand.DISCONNECT);
		Message<?> resultMessage = stompHandler.preSend(sendMessage, null);

		// Then
		StompHeaderAccessor resultHeaderAccessor = StompHeaderAccessor.wrap(resultMessage);
		assertThat(resultHeaderAccessor.getCommand()).isEqualTo(StompCommand.DISCONNECT);
	}

	private Message<?> setTestMessage(StompCommand command) {

		header.put("simpSessionAttributes", sessionAttributes);
		header.put("nativeHeaders", nativeHeaders);
		header.put("stompCommand", command);

		MessageHeaders messageHeaders = new MessageHeaders(header);

		return MessageBuilder.createMessage(MESSAGE, messageHeaders);
	}
}
