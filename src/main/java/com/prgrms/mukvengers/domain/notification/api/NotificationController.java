package com.prgrms.mukvengers.domain.notification.api;

import static org.springframework.http.MediaType.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponses;
import com.prgrms.mukvengers.domain.notification.service.NotificationService;
import com.prgrms.mukvengers.global.security.token.dto.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	/**
	 * 로그인 한 유저 sse 연결
	 */
	@GetMapping(value = "/subscribe", produces = TEXT_EVENT_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public SseEmitter subscribe(@AuthenticationPrincipal JwtAuthentication user,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return notificationService.subscribe(user.id(), lastEventId);
	}

	/**
	 * 로그인 한 유저의 모든 알림 조회
	 */
	@GetMapping(value = "/notifications", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<NotificationResponses> notifications(@AuthenticationPrincipal JwtAuthentication user) {
		return ResponseEntity.ok().body(notificationService.findAllById(user.id()));
	}

	/**
	 * 알림 상태를 읽음으로 변경
	 */
	@PatchMapping(value = "/notifications/{id}")
	public ResponseEntity<Void> readNotification(@AuthenticationPrincipal JwtAuthentication user,
		@PathVariable Long id) {
		notificationService.readNotification(user.id(), id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
