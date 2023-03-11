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

	public static final String DEFAULT_PATH = "/topic/public/";

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final CrewMemberRepository crewMemberRepository;

	// websocket을 통해 들어온 요청이 처리 되기전 실행된다.
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();

		if (StompCommand.CONNECT.equals(command)) { // websocket 연결요청 -> JWT 인증

			// JWT 인증
			User user = getUserByAuthorizationHeader(
				accessor.getFirstNativeHeader("Authorization"));
			// 인증 후 데이터를 헤더에 추가
			setValue(accessor, "userId", user.getId());
			setValue(accessor, "username", user.getNickname());
			setValue(accessor, "profileImgUrl", user.getProfileImgUrl());

		} else if (StompCommand.SUBSCRIBE.equals(command)) { // 채팅룸 구독요청(진입) -> CrewMember인지 검증

			Long userId = (Long)getValue(accessor, "userId");
			Long crewId = parseCrewIdFromPath(accessor);
			log.debug("userId : " + userId + "crewId : " + crewId);
			setValue(accessor, "crewId", crewId);
			validateUserInCrew(userId, crewId);

		} else if (StompCommand.DISCONNECT == command) { // Websocket 연결 종료
			Long userId = (Long)getValue(accessor, "userId");
			log.info("DISCONNECTED userId : {}", userId);
		}

		log.info("header : " + message.getHeaders());
		log.info("message:" + message);

		return message;
	}

	private User getUserByAuthorizationHeader(String authHeaderValue) {

		String accessToken = getTokenByAuthorizationHeader(authHeaderValue);

		Claims claims = jwtTokenProvider.getClaims(accessToken);
		Long userId = claims.get("userId", Long.class);

		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}

	private String getTokenByAuthorizationHeader(String authHeaderValue) {

		if (Objects.isNull(authHeaderValue) || authHeaderValue.isBlank()) {
			throw new WebSocketException("authHeaderValue: " + authHeaderValue);
		}

		String accessToken = ExtractUtil.extractToken(authHeaderValue);
		jwtTokenProvider.validateToken(accessToken); // 예외 발생할 수 있음

		return accessToken;
	}

	private Long parseCrewIdFromPath(StompHeaderAccessor accessor) {
		String destination = accessor.getDestination();
		return Long.parseLong(destination.substring(DEFAULT_PATH.length()));
	}

	private void validateUserInCrew(Long userId, Long crewId) {
		crewMemberRepository.findCrewMemberByCrewIdAndUserId(crewId, userId)
			.orElseThrow(() -> new WebSocketException(
				String.format("crew Id : {} userId : {} 로 조회된 결과가 없습니다.", crewId, userId)));
	}

	private Object getValue(StompHeaderAccessor accessor, String key) {
		Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
		Object value = sessionAttributes.get(key);

		if (Objects.isNull(value)) {
			throw new WebSocketException(key + " 에 해당하는 값이 없습니다.");
		}

		return value;
	}

	private void setValue(StompHeaderAccessor accessor, String key, Object value) {
		Map<String, Object> sessionAttributes = getSessionAttributes(accessor);
		sessionAttributes.put(key, value);
	}

	private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (Objects.isNull(sessionAttributes)) {
			throw new WebSocketException("SessionAttributes가 null입니다.");
		}
		return sessionAttributes;
	}

}
