package com.prgrms.mukvengers.domain.notification;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.prgrms.mukvengers.domain.notification.model.vo.NotificationType;
import com.prgrms.mukvengers.domain.notification.service.NotificationService;
import com.prgrms.mukvengers.domain.proposal.dto.request.CreateProposalRequest;
import com.prgrms.mukvengers.domain.proposal.dto.request.UpdateProposalRequest;
import com.prgrms.mukvengers.global.utils.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PushNotificationAspect {

	private final NotificationService notificationService;
	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@AfterReturning("@annotation(com.prgrms.mukvengers.domain.notification.annotation.PushNotification) && args(proposalRequest, ..)")
	public void proposalArriveNotificate(JoinPoint joinPoint, CreateProposalRequest proposalRequest) {
		String message = MessageUtil.getMessage("proposal.arrived");

		threadPoolTaskExecutor.execute(() ->
			notificationService.send(proposalRequest.leaderId(), message, NotificationType.INFO));
	}

	@AfterReturning("@annotation(com.prgrms.mukvengers.domain.notification.annotation.PushNotification) && args(proposalRequest, userId, ..)")
	public void proposalUpdateNotificate(JoinPoint joinPoint, UpdateProposalRequest proposalRequest, Long userId) {
		String proposalStatus = proposalRequest.proposalStatus();
		if (proposalStatus.equals("승인")) {
			String message = MessageUtil.getMessage("proposal.accepted");

			threadPoolTaskExecutor.execute(() -> {
				notificationService.send(userId, message, NotificationType.APPROVE);
			});
		} else if (proposalStatus.equals("거절")) {
			String message = MessageUtil.getMessage("proposal.rejected");

			threadPoolTaskExecutor.execute(() -> {
				notificationService.send(userId, message, NotificationType.REJECT);
			});
			log.info("거절 알림 발송");
		}
	}
}
