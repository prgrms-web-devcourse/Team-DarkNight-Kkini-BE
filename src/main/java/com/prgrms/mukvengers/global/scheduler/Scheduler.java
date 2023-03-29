package com.prgrms.mukvengers.global.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.crew.repository.CrewRepository;
import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

	private final CrewRepository crewRepository;
	private final ProposalRepository proposalRepository;

	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
	@Transactional
	public void changStatusOvertimeCrew() {
		crewRepository.updateAllStatusToFinish(LocalDateTime.now());
	}

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	@Transactional
	public void deleteProposalOverTimeCrew() {
		proposalRepository.deleteProposalsByPromiseTime(LocalDateTime.now());
	}
}
