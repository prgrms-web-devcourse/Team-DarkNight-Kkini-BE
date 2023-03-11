package com.prgrms.mukvengers.domain.proposal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

	List<Proposal> findAllByLeaderIdOrderByCreatedAtDesc(@Param("userId") Long userId);

	List<Proposal> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

	@Query("""
		SELECT p.status
		FROM Proposal p
		WHERE p.user.id = :userId AND p.crewId = :crewId
		""")
	Optional<ProposalStatus> findByUserIdAndCrewId(@Param("userId") Long userId, @Param("crewId") Long crewId);

}
