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

	//최초 구독 요청
	public SseEmitter subscribe(Long userId, String lastEventId) {
		String emitterId = userId + "_" + System.currentTimeMillis();
		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

		// 503 에러를 방지하기 위한 더미 이벤트 전송
		sendNotification(emitter, emitterId, "EventStream Created. [userId=" + userId + "]");

		// 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
		if (!lastEventId.isEmpty()) {
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
			events.entrySet().stream()
				.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
				.forEach(entry -> sendNotification(emitter, entry.getKey(), entry.getValue()));
		}

		return emitter;
	}

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

	@Transactional
	public Notification save(Long receiverId, String content, NotificationType type) {
		validateReceiver(receiverId);

		Notification notification = notificationFactory.createNotification(receiverId, content, type);
		notificationRepository.save(notification);

		return notification;
	}

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

	@Transactional
	public void readNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new NotificationNotFoundException(notificationId));

		notification.read();
	}

	private void validateReceiver(Long receiverId) {
		if (!userRepository.existsById(receiverId)) {
			throw new UserNotFoundException(receiverId);
		}

	}

}
