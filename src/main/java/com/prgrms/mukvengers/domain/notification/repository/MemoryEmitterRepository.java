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

	@Override
	public SseEmitter save(String id, SseEmitter sseEmitter) {
		emitters.put(id, sseEmitter);
		return sseEmitter;
	}

	@Override
	public void saveEventCache(String eventCacheId, Object event) {
		eventCache.put(eventCacheId, event);
	}

	@Override
	public Map<String, SseEmitter> findAllStartWithById(String userId) {
		delimiter = userId + "_";

		return emitters.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(delimiter))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public Map<String, Object> findAllEventCacheStartWithId(String userId) {
		delimiter = userId + "_";

		return eventCache.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(delimiter))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

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
