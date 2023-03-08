package com.prgrms.mukvengers.domain.crewmember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

	@Query("""
		SELECT cm
		FROM CrewMember cm
		WHERE cm.crew.id= :crewId AND cm.userId= :userId
		""")
	Optional<CrewMember> findCrewMemberByCrewIdAndUserId(@Param(value = "crewId") Long crewId,
		@Param(value = "userId") Long userId);

	@Query(value = """
		SELECT cm.crew
		FROM CrewMember cm
		WHERE cm.userId = :userId
		ORDER BY CASE WHEN cm.crew.status = 'RECRUITING' THEN 0
					  WHEN cm.crew.status = 'CLOSE' THEN 1
					  ELSE 2 END
		""")
	List<Crew> findAllByUserIdOrderByStatus(@Param("userId") Long userId);

	Integer countCrewMemberByCrewId(@Param(value = "crewId") Long crewId);

	List<CrewMember> findAllByCrewId(@Param(value = "crewId") Long crewId);
}
