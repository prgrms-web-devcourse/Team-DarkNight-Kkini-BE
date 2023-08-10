package com.prgrms.mukvengers.domain.proposal.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.mukvengers.domain.proposal.repository.ProposalRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProposalScheduler {

	private final ProposalRepository proposalRepository;

	@Async
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	@Transactional
	public void deleteProposalOverTimeCrew() {
		proposalRepository.deleteProposalsByPromiseTime(LocalDateTime.now());
	}
}
