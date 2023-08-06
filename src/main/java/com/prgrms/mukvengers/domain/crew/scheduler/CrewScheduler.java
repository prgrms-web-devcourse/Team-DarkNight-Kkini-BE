package com.prgrms.mukvengers.domain.crew.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CrewScheduler {

	private final CrewRepository crewRepository;

	@Async
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
	@Transactional
	public void changStatusOvertimeCrew() {
		crewRepository.updateAllStatusToFinish(LocalDateTime.now());
	}
}
