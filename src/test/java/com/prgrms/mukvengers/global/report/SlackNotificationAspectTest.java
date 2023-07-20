package com.prgrms.mukvengers.global.report;

import static org.mockito.BDDMockito.*;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.prgrms.mukvengers.base.SliceTest;

class SlackNotificationAspectTest extends SliceTest {

	private final String TEST_WEBHOOK = "https://hooks.slack.com/services/abcd";

	private final Exception exception = new RuntimeException("test exception message");

	@Mock
	private HttpServletRequest request;

	@Mock
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Mock
	private Environment env;

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	private SlackNotificationAspect slackNotificationAspect;

	@BeforeEach
	void setUp() {
		slackNotificationAspect = new SlackNotificationAspect(TEST_WEBHOOK, threadPoolTaskExecutor, env);
	}

	@Test
	@DisplayName("[성공] 예외가 발생하면 슬랙으로 알림이 전송된다.")
	void slackNotificateTest() throws Throwable { // 비동기 실행되는 메서드(sendSlackMessage)는 어떻게 테스트해야하는가?
		// Given
		given(request.getRequestURL()).willReturn(new StringBuffer("http://localhost:8080"));
		given(request.getMethod()).willReturn("GET");
		given(request.getRemoteAddr()).willReturn("192.0.0.2");

		// When
		slackNotificationAspect.slackNotificate(proceedingJoinPoint, request, exception);

		// Then
		then(proceedingJoinPoint).should().proceed(); // AOP 동작확인
		then(threadPoolTaskExecutor).should().execute(any(Runnable.class)); // 비동기 실행확인
	}

}

