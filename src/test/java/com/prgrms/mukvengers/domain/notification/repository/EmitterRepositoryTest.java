package com.prgrms.mukvengers.domain.notification.repository;

import static com.prgrms.mukvengers.domain.notification.model.vo.NotificationType.*;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.mukvengers.domain.notification.model.Notification;
import com.prgrms.mukvengers.domain.user.model.User;
import com.prgrms.mukvengers.domain.user.repository.UserRepository;
import com.prgrms.mukvengers.utils.NotificationObjectProvider;
import com.prgrms.mukvengers.utils.UserObjectProvider;

@SpringBootTest
class EmitterRepositoryTest {

	@Autowired
	private EmitterRepository emitterRepository;

	@Autowired
	private UserRepository userRepository;

	private Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;
	private User user;

	@BeforeEach
	void setup() {
		user = UserObjectProvider.createUser();
		userRepository.save(user);
	}

	@Test
	@DisplayName("[성공] 새로운 Emitter를 추가한다.")
	public void save() throws Exception {
		//given
		Long memberId = 1L;
		String emitterId = memberId + "_" + System.currentTimeMillis();
		SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

		//when, then
		Assertions.assertDoesNotThrow(() -> emitterRepository.save(emitterId, sseEmitter));
	}

	@Test
	@DisplayName("[성공] 수신한 이벤트를 캐시에 저장한다.")
	public void saveEventCache() {
		//given
		String eventCacheId = user.getId() + "_" + System.currentTimeMillis();
		Notification notification = NotificationObjectProvider.createNotification("알람왔어요", user.getId(), INFO);

		//when, then
		Assertions.assertDoesNotThrow(() -> emitterRepository.saveEventCache(eventCacheId, notification));
	}

	@Test
	@DisplayName("[성공] 특정 회원의 모든 Emitter를 찾는다")
	public void findAllEmitterStartWithByMemberId() throws Exception {
		//given
		Long memberId = 10L;
		String emitterId1 = memberId + "_" + System.currentTimeMillis();
		emitterRepository.save(emitterId1, new SseEmitter(DEFAULT_TIMEOUT));

		Thread.sleep(100);
		String emitterId2 = memberId + "_" + System.currentTimeMillis();
		emitterRepository.save(emitterId2, new SseEmitter(DEFAULT_TIMEOUT));

		Thread.sleep(100);
		String emitterId3 = memberId + "_" + System.currentTimeMillis();
		emitterRepository.save(emitterId3, new SseEmitter(DEFAULT_TIMEOUT));

		//when
		Map<String, SseEmitter> ActualResult = emitterRepository.findAllStartWithById(String.valueOf(10L));

		//then
		Assertions.assertEquals(3, ActualResult.size());
	}

	@Test
	@DisplayName("[성공] 회원에게 수신된 이벤트를 캐시에서 모두 찾는다.")
	public void findAllEventCacheStartWithByMemberId() throws Exception {
		//given
		Long memberId = 1L;
		String eventCacheId1 = memberId + "_" + System.currentTimeMillis();
		Notification notification1 = NotificationObjectProvider.createNotification("알람1", user.getId(), INFO);
		emitterRepository.saveEventCache(eventCacheId1, notification1);

		Thread.sleep(100);
		String eventCacheId2 = memberId + "_" + System.currentTimeMillis();
		Notification notification2 = NotificationObjectProvider.createNotification("알람2", user.getId(), INFO);
		emitterRepository.saveEventCache(eventCacheId2, notification2);

		Thread.sleep(100);
		String eventCacheId3 = memberId + "_" + System.currentTimeMillis();
		Notification notification3 = NotificationObjectProvider.createNotification("알람3", user.getId(), INFO);
		emitterRepository.saveEventCache(eventCacheId3, notification3);

		//when
		Map<String, Object> ActualResult = emitterRepository.findAllEventCacheStartWithId(String.valueOf(memberId));

		//then
		Assertions.assertEquals(3, ActualResult.size());
	}

	@Test
	@DisplayName("[성공] ID를 통해 Emitter를 Repository에서 제거한다.")
	public void deleteById() {
		//given
		Long memberId = 1L;
		String emitterId = memberId + "_" + System.currentTimeMillis();
		SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

		//when
		emitterRepository.save(emitterId, sseEmitter);
		emitterRepository.deleteById(emitterId);

		//then
		Assertions.assertEquals(0, emitterRepository.findAllStartWithById(emitterId).size());
	}

	@Test
	@DisplayName("[성공] 저장된 모든 Emitter를 제거한다.")
	public void deleteAllEmitterStartWithId() throws Exception {
		//given
		Long memberId = 1L;
		String emitterId1 = memberId + "_" + System.currentTimeMillis();
		emitterRepository.save(emitterId1, new SseEmitter(DEFAULT_TIMEOUT));

		Thread.sleep(100);
		String emitterId2 = memberId + "_" + System.currentTimeMillis();
		emitterRepository.save(emitterId2, new SseEmitter(DEFAULT_TIMEOUT));

		//when
		emitterRepository.deleteAllStartWithId(String.valueOf(memberId));

		//then
		Assertions.assertEquals(0, emitterRepository.findAllStartWithById(String.valueOf(memberId)).size());
	}

	@Test
	@DisplayName("[성공] 수신한 이벤트를 캐시에 저장한다.")
	public void deleteAllEventCacheStartWithId() throws Exception {
		//given
		Long memberId = 1L;
		String eventCacheId1 = memberId + "_" + System.currentTimeMillis();
		Notification notification1 = NotificationObjectProvider.createNotification("알람1", user.getId(), INFO);
		emitterRepository.saveEventCache(eventCacheId1, notification1);

		Thread.sleep(100);
		String eventCacheId2 = memberId + "_" + System.currentTimeMillis();
		Notification notification2 = NotificationObjectProvider.createNotification("알람1", user.getId(), INFO);
		emitterRepository.saveEventCache(eventCacheId2, notification2);

		//when
		emitterRepository.deleteAllEventCacheStartWithId(String.valueOf(memberId));

		//then
		Assertions.assertEquals(0, emitterRepository.findAllEventCacheStartWithId(String.valueOf(memberId)).size());
	}
}