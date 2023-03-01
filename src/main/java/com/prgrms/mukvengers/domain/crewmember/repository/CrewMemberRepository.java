package com.prgrms.mukvengers.domain.crewmember.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

	@Query("""
		SELECT cm
		FROM CrewMember cm
		WHERE cm.crew.id= :crewId AND cm.user.id = :reviewMemberId
		""")
	Optional<CrewMember> findCrewMemberByCrewId(@Param(value = "crewId") Long crewId, @Param(value = "reviewMemberId") Long reviewMemberId);
}
