package com.prgrms.mukvengers.global.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

	private final CrewRepository crewRepository;

	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
	@Transactional
	public void changStatusOvertimeCrew() {
		crewRepository.updateStatusAll(LocalDateTime.now());
	}
}
