package com.prgrms.mukvengers.domain.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class MemoryEmitterRepository implements EmitterRepository {
	public final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();
	private String delimiter;

	/**
	 * 알림 발송 전 Notification 객체를 emitters에 저장하게 됩니다.
	 * [key = userId_현재시간, value = 연결 요청 시 생성한 Emitter 객체]
	 */
	@Override
	public SseEmitter save(String id, SseEmitter sseEmitter) {
		emitters.put(id, sseEmitter);
		return sseEmitter;
	}

	/**
	 * 알림 발송 후 Notification 객체를 EventCache에 저장하게 됩니다.
	 */
	@Override
	public void saveEventCache(String eventCacheId, Object event) {
		eventCache.put(eventCacheId, event);
	}

	/**
	 * 유저 ID를 이용해 특정 유저의 SseEmitter를 조회합니다.
	 */
	@Override
	public Map<String, SseEmitter> findAllStartWithById(String userId) {
		delimiter = userId + "_";

		return emitters.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(delimiter))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * 유저 ID를 이용해 EventCache에서 특정 유저에게 전송되었던 알림들을 조회합니다.
	 */
	@Override
	public Map<String, Object> findAllEventCacheStartWithId(String userId) {
		delimiter = userId + "_";

		return eventCache.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(delimiter))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * emitters에서 특정 유저의 Emitter를 일괄 삭제합니다.
	 * Emitter의 timeout, OnCompletion이 호출될 경우 콜백으로 실행됩니다.
	 */
	@Override
	public void deleteAllStartWithId(String userId) {
		delimiter = userId + "_";

		emitters.forEach(
			(key, emitter) -> {
				if (key.startsWith(delimiter)) {
					emitters.remove(key);
				}
			}
		);
	}

	@Override
	public void deleteById(String id) {
		emitters.remove(id);
	}

	/**
	 * evnetCache에서 특정 유저의 알람 목록을 삭제합니다.
	 */
	@Override
	public void deleteAllEventCacheStartWithId(String userId) {
		delimiter = userId + "_";

		eventCache.forEach(
			(key, data) -> {
				if (key.startsWith(delimiter)) {
					eventCache.remove(key);
				}
			}
		);
	}
}
