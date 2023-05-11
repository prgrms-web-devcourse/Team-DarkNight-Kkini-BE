package com.prgrms.mukvengers.domain.notification.repository;

import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
	SseEmitter save(String id, SseEmitter sseEmitter);

	void saveEventCache(String eventCacheId, Object event);

	Map<String, SseEmitter> findAllStartWithById(String userId);

	Map<String, Object> findAllEventCacheStartWithId(String userId);

	void deleteAllStartWithId(String userId);

	void deleteById(String id);

	void deleteAllEventCacheStartWithId(String userId);
}
