package com.prgrms.mukvengers.global.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;

import lombok.RequiredArgsConstructor;

@Component
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

	private final CrewRepository crewRepository;
	private final ProposalRepository proposalRepository;

	@Async
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
	@Transactional
	public void changStatusOvertimeCrew() {
		crewRepository.updateAllStatusToFinish(LocalDateTime.now());
	}

	@Async
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	@Transactional
	public void deleteProposalOverTimeCrew() {
		proposalRepository.deleteProposalsByPromiseTime(LocalDateTime.now());
	}
}
