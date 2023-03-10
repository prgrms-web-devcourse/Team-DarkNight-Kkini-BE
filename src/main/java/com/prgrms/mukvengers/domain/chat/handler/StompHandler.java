package com.prgrms.mukvengers.domain.chat.handler;

import java.util.Map;
import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.domain.chat.exception.WebSocketException;
import com.prgrms.mukvengers.domain.crewmember.repository.CrewMemberRepository;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.global.security.jwt.JwtTokenProvider;
import com.prgrms.mukvengers.global.utils.ExtractUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	public static final String USER_ID = "userId";
	public static final String CREW_ID = "crewId";

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final CrewMemberRepository crewMemberRepository;

	// websocket을 통해 들어온 요청이 처리 되기전 실행된다.
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) { // websocket 연결요청 -> JWT 인증

			User user = getUserByAuthorizationHeader(
				accessor.getFirstNativeHeader("Authorization"));

			setValue(accessor, USER_ID, user.getId());
			setValue(accessor, "username", user.getNickname());
			setValue(accessor, "profileImgUrl", user.getProfileImgUrl());

			log.error("헤더 : " + message.getHeaders());
			log.error("message:" + message);

		} else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) { // 채팅룸 구독요청(진입) -> CrewMember인지 검증

			validateUserInCrew(accessor);

		} else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
			// 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
			Long userId = (Long)getValue(accessor, USER_ID);
			Long crewId = (Long)getValue(accessor, CREW_ID);
			log.info("DISCONNECTED crewId : {}, userId : {}", crewId, userId);
		}

		return message;
	}

	private User getUserByAuthorizationHeader(String authHeaderValue) {

		String accessToken = getTokenByAuthorizationHeader(authHeaderValue);

		Claims claims = jwtTokenProvider.getClaims(accessToken);
		Long userId = claims.get(USER_ID, Long.class);

		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}

	private String getTokenByAuthorizationHeader(String authHeaderValue) {

		if (Objects.isNull(authHeaderValue) || authHeaderValue.isBlank()) {
			throw new WebSocketException();
		}

		String accessToken = ExtractUtil.extractToken(authHeaderValue);
		jwtTokenProvider.validateToken(accessToken);

		return accessToken;
	}

	private void validateUserInCrew(StompHeaderAccessor accessor) {
		Long userId = (Long)getValue(accessor, USER_ID);
		Long crewId = (Long)getValue(accessor, CREW_ID);

		crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId)
			.orElseThrow(WebSocketException::new);
	}

	private Object getValue(StompHeaderAccessor accessor, String valueName) {
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (Objects.isNull(sessionAttributes)) {
			throw new WebSocketException();
		}

		Object value = sessionAttributes.get(valueName);

		if (Objects.isNull(value)) {
			throw new WebSocketException();
		}

		return value;
	}

	private void setValue(StompHeaderAccessor accessor, String key, Object value) {
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (Objects.isNull(sessionAttributes)) {
			throw new WebSocketException();
		}

		sessionAttributes.put(key, value);
	}

}
