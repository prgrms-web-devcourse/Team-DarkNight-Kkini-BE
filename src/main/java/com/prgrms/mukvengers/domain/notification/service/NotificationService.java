package com.prgrms.mukvengers.domain.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponse;
import com.prgrms.mukvengers.domain.notification.dto.response.NotificationResponses;
import com.prgrms.mukvengers.domain.notification.exception.NotificationNotFoundException;
import com.prgrms.mukvengers.domain.notification.mapper.NotificationMapper;
import com.prgrms.mukvengers.domain.notification.model.Notification;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;
import com.prgrms.mukvengers.domain.notification.repository.EmitterRepository;
import com.prgrms.mukvengers.domain.notification.repository.NotificationRepository;
import com.prgrms.mukvengers.domain.notification.util.NotificationFactory;
import com.prgrms.mukvengers.domain.user.exception.UserNotFoundException;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

	private final NotificationRepository notificationRepository;
	private final EmitterRepository emitterRepository;
	private final NotificationMapper notificationMapper;
	private final NotificationFactory notificationFactory;
	private final UserRepository userRepository;

	/**
	 *  /subscribe로 연결 요청 시 SseEmitter를 생성합니다.
	 *  이후 더미 데이터를 발송합니다.
	 *  onCompletion()은 SseEmitter가 오류나 서버 종료에 의해 닫힐때 실행됩니다.
	 *	따라서 onCompletion이 실행될 때, Timeout이 되었을 때 콜백으로 Emitter를 삭제하도록 하였습니다.
	 */
	public SseEmitter subscribe(Long userId, String lastEventId) {
		String emitterId = userId + "_" + System.currentTimeMillis();
		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

		/**
		 503 에러를 방지하기 위한 더미 이벤트 전송
		 클라이언트는 SSE Timeout이 될 경우 자동으로 재연결을 시도합니다.
		 재연결 시 한 번도 데이터를 전송한 적이 없다면 503 에러가 발생하므로 최초 연결 시 더미 이벤트를 전송합니다.
		 */
		sendNotification(emitter, emitterId, "EventStream Created. [userId=" + userId + "]");

		/**
		 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방합니다.
		 클라이언트의 요청 헤더에 lastEventId값이 있는 경우 lastEventId보다 더 큰(더 나중에 생성된) emitter를 찾아서 발송합니다.
		 */
		if (!lastEventId.isEmpty()) {
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
			events.entrySet().stream()
				.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
				.forEach(entry -> sendNotification(emitter, entry.getKey(), entry.getValue()));
		}

		return emitter;
	}

	/**
	 특정 SseEmitter를 이용해 알림을 보냅니다.
	 SseEmitter는 최초 연결 시 생성되며, 해당 SseEmitter를 생성한 클라이언트로 알림을 발송하게 됩니다.
	 */
	private void sendNotification(SseEmitter emitter, String emitterId, Object data) {
		try {
			emitter.send(SseEmitter.event()
				.id(emitterId)
				.data(data));

		} catch (IOException exception) {
			emitterRepository.deleteById(emitterId);
			log.error("SSE 연결 오류", exception);
		}
	}

	/**
	 알림 발송 메소드
	 다른 서비스 레이어에서 사용하게 될 메소드입니다.
	 */
	public void send(Long receiverId, String content, NotificationType type) {
		validateReceiver(receiverId);

		Notification notification = save(receiverId, content, type);
		String id = String.valueOf(receiverId);

		Map<String, SseEmitter> emitters = emitterRepository.findAllStartWithById(id);

		emitters.forEach(
			(key, emitter) -> {
				emitterRepository.saveEventCache(key, notification);
				sendNotification(emitter, key, notificationMapper.toNotificationResponse(notification));
			}
		);
	}

	/**
	 발송된 알림을 DB에 저장합니다.
	 */
	@Transactional
	public Notification save(Long receiverId, String content, NotificationType type) {
		validateReceiver(receiverId);

		Notification notification = notificationFactory.createNotification(receiverId, content, type);
		notificationRepository.save(notification);

		return notification;
	}

	/**
	 해당 유저가 수신한 알림 목록을 전부 가져옵니다.
	 */
	@Transactional
	public NotificationResponses findAllById(Long userId) {
		validateReceiver(userId);

		List<NotificationResponse> responses = notificationRepository.findAllByReceiverId(userId)
			.stream()
			.map(notificationMapper::toNotificationResponse)
			.toList();

		long unreadCount = responses.stream()
			.filter(notification -> !notification.isRead())
			.count();

		return new NotificationResponses(responses, unreadCount);
	}

	/**
	 알림을 읽음처리합니다.
	 */
	@Transactional
	public void readNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new NotificationNotFoundException(notificationId));

		notification.read();
	}

	/*
		유저의 ID를 이용해 존재하는 사용자인지 검증합니다.
	 */
	private void validateReceiver(Long receiverId) {
		if (!userRepository.existsById(receiverId)) {
			throw new UserNotFoundException(receiverId);
		}

	}

}
