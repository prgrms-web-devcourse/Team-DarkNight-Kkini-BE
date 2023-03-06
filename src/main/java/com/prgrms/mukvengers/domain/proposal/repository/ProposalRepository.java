package com.prgrms.mukvengers.domain.proposal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.proposal.model.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

	List<Proposal> findAllByLeaderId(@Param("userId") Long userId);

}
