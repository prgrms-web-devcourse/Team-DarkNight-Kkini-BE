package com.prgrms.mukvengers.domain.proposal.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
		""")
	Page<Proposal> findAllByLeaderId(@Param("userId") Long userId, Pageable pageable);

	Page<Proposal> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

	@Query("""
		SELECT p.status
		FROM Proposal p
		WHERE p.user.id = :userId AND p.crewId = :crewId
		""")
	Optional<ProposalStatus> findByUserIdAndCrewId(@Param("userId") Long userId, @Param("crewId") Long crewId);

	@Modifying
	@Query(value = """
		DELETE
		FROM Proposal p
		WHERE p.crewId IN (SELECT c.id FROM Crew c WHERE c.promiseTime < :time)
		""")
	int deleteProposalsByPromiseTime(@Param("time") LocalDateTime now);

}
