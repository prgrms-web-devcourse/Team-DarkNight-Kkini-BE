package com.prgrms.mukvengers.domain.proposal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.mukvengers.domain.proposal.model.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
