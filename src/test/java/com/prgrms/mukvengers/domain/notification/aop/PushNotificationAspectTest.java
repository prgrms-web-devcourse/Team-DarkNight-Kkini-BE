package com.prgrms.mukvengers.domain.notification.aop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import com.prgrms.mukvengers.base.SliceTest;
import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;
import com.prgrms.mukvengers.domain.notification.service.NotificationService;
import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.request.UpdateProposalRequest;
import com.prgrms.mukvengers.global.utils.MessageUtil;

class PushNotificationAspectTest extends SliceTest {

	private PushNotificationAspect aspect;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Mock
	private MockedStatic<MessageUtil> messageUtil;

	@Mock
	private JoinPoint joinPoint;

	@BeforeTestClass
	void setup() {
		messageUtil = mockStatic(MessageUtil.class);
	}

	@BeforeEach
	void init() {
		aspect = new PushNotificationAspect(notificationService, threadPoolTaskExecutor);
	}

	@AfterEach
	void clear() {
		messageUtil.close();
	}

	@Test
	@DisplayName("[성공] 신청서가 접수되는 경우 notificationService의 send 메소드가 호출된다.")
	void proposal_arrive_sendNotification() throws InterruptedException {
		// Given
		String message = "Proposal Arrived";
		Long receiverId = 123L;

		CreateProposalRequest proposalRequest = new CreateProposalRequest(123L, "밥 같이 먹고 싶어용");
		given(MessageUtil.getMessage(any())).willReturn(message);

		//멀티 쓰레드 환경에서 동기화를 위해 사용되는 클래스. 실행 횟수를 기록할 수 있습니다.
		CountDownLatch latch = new CountDownLatch(1);

		doAnswer(invocation -> {
			//doAnswer는 threadPoolTaskExecutor.execute 하나를 모킹하고 있으므로 0번째 인자는 Runnable 클래스입니다.
			Runnable runnable = invocation.getArgument(0);

			runnable.run();  //비동기 작업 실행

			latch.countDown(); //CountDownLatch의 값을 1 -> 0로 감소
			return null;
		}).when(threadPoolTaskExecutor).execute(any(Runnable.class));

		// When
		aspect.proposalArriveNotificate(joinPoint, proposalRequest);

		//1초 이내에 CountDownLatch의 값이 0이 되기를 기대합니다.
		//1초 이내에 0이 된다면 true, 아닌 경우 false를 반환합니다.
		boolean completed = latch.await(1, TimeUnit.SECONDS);

		// Then
		verify(threadPoolTaskExecutor).execute(any(Runnable.class));
		verify(notificationService).send(eq(receiverId), eq(message), eq(NotificationType.INFO));

		assertTrue(completed);
	}

	@Test
	@DisplayName("[성공] 신청서가 승인된 경우 notificationService의 send 메소드가 호출된다.")
	void proposal_approve_sendNotification() throws InterruptedException {
		// Given
		UpdateProposalRequest proposalRequest = new UpdateProposalRequest("승인");
		String message = "Proposal Accepted";
		Long receiverId = 456L;

		given(MessageUtil.getMessage(any())).willReturn(message);

		//실행 횟수 기록
		CountDownLatch latch = new CountDownLatch(1);

		doAnswer(invocation -> {
			Runnable runnable = invocation.getArgument(0);

			runnable.run();  //비동기 작업 실행

			latch.countDown(); //CountDownLatch의 값을 1 -> 0로 감소
			return null;
		}).when(threadPoolTaskExecutor).execute(any(Runnable.class));

		// When
		aspect.proposalUpdateNotificate(joinPoint, proposalRequest, receiverId);
		boolean completed = latch.await(1, TimeUnit.SECONDS); //실행되었는지 검사

		// Then
		verify(threadPoolTaskExecutor).execute(any(Runnable.class));
		verify(notificationService).send(eq(receiverId), eq(message), eq(NotificationType.APPROVE));

		assertTrue(completed);
	}

	@Test
	@DisplayName("[성공] 신청서가 거절되는 경우 notificationService의 send 메소드가 호출된다.")
	void proposal_reject_sendNotification() throws InterruptedException {
		// Given
		UpdateProposalRequest proposalRequest = new UpdateProposalRequest("거절");
		String message = "Proposal Rejected";
		Long receiverId = 456L;

		given(MessageUtil.getMessage(any())).willReturn(message);

		CountDownLatch latch = new CountDownLatch(1);

		doAnswer(invocation -> {
			Runnable runnable = invocation.getArgument(0);

			runnable.run();  //비동기 작업 실행

			latch.countDown();
			return null;
		}).when(threadPoolTaskExecutor).execute(any(Runnable.class));

		// When
		aspect.proposalUpdateNotificate(joinPoint, proposalRequest, receiverId);
		boolean completed = latch.await(1, TimeUnit.SECONDS); //실행되었는지 검사

		// Then
		verify(threadPoolTaskExecutor).execute(any(Runnable.class));
		verify(notificationService).send(eq(receiverId), eq(message), eq(NotificationType.REJECT));

		assertTrue(completed);
	}
}