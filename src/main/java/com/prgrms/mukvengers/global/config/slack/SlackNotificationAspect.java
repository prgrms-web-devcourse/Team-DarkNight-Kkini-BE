package com.prgrms.mukvengers.global.config.slack;

import static java.util.Collections.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import com.prgrms.mukvengers.global.common.dto.RequestInfo;

@Aspect
@Component
@Profile(value = {"dev", "prod"})
public class SlackNotificationAspect {
	private final SlackApi slackApi;
	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private final Environment env;

	public SlackNotificationAspect(@Value("${spring.slack.webhook}") String webhook,
		ThreadPoolTaskExecutor threadPoolTaskExecutor, Environment env) {
		this.slackApi = new SlackApi(webhook);
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
		this.env = env;
	}

	@Around("@annotation(com.prgrms.mukvengers.global.common.annotation.SlackNotification) && args(request, e)")
	public void slackNotificate(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request,
		Exception e) throws Throwable {

		proceedingJoinPoint.proceed();

		RequestInfo requestInfo = new RequestInfo(request);

		threadPoolTaskExecutor.execute(() -> {
			sendSlackMessage(requestInfo, e);
		});
	}

	private void sendSlackMessage(RequestInfo request, Exception e) {
		SlackAttachment slackAttachment = new SlackAttachment();
		slackAttachment.setFallback("Error");
		slackAttachment.setColor("danger");

		slackAttachment.setFields(
			List.of(
				new SlackField().setTitle("Exception class").setValue(e.getClass().getCanonicalName()),
				new SlackField().setTitle("예외 메시지").setValue(e.getMessage()),
				new SlackField().setTitle("Request URI").setValue(request.requestURL()),
				new SlackField().setTitle("Request Method").setValue(request.method()),
				new SlackField().setTitle("요청 시간")
					.setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))),
				new SlackField().setTitle("Request IP").setValue(request.remoteAddress()),
				new SlackField().setTitle("Profile 정보").setValue(Arrays.toString(env.getActiveProfiles()))
			)
		);

		SlackMessage slackMessage = new SlackMessage();
		slackMessage.setAttachments(singletonList(slackAttachment));
		slackMessage.setIcon(":ghost:");
		slackMessage.setText("Error Detect");
		slackMessage.setUsername("kkiniRobot");

		slackApi.call(slackMessage);
	}
}
