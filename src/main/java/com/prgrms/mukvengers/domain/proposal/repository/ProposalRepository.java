package com.prgrms.mukvengers.domain.proposal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.proposal.model.Proposal;
import com.prgrms.mukvengers.domain.proposal.model.vo.ProposalStatus;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

	@Query(value = """
		SELECT p
		FROM Proposal p
		WHERE p.user.id = :userId AND p.crewId = :crewId
		""")
	Optional<Proposal> findProposalByUserIdAndCrewId(@Param("userId") Long userId, @Param("crewId") Long crewId);

	@Query("""
		SELECT p
		FROM Proposal p
		WHERE p.leaderId = :userId
		ORDER BY CASE WHEN p.status = 'WAITING' THEN 0
					  WHEN p.status = 'APPROVE' THEN 1
					  WHEN p.status = 'REFUSE' THEN 2
					  ELSE 3 END
		""")
	List<Proposal> findAllByLeaderIdOrderStatus(@Param("userId") Long userId);

	List<Proposal> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

	@Query("""
		SELECT p.status
		FROM Proposal p
		WHERE p.user.id = :userId AND p.crewId = :crewId
		""")
	Optional<ProposalStatus> findByUserIdAndCrewId(@Param("userId") Long userId, @Param("crewId") Long crewId);

}
