package com.prgrms.mukvengers.domain.crewmember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.crew.model.Crew;
import com.prgrms.mukvengers.domain.crewmember.model.CrewMember;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

	Optional<CrewMember> findCrewMemberByCrewIdAndUserId(@Param(value = "crewId") Long crewId,
		@Param(value = "userId") Long userId);

	@Query(value = """
		SELECT cm.crew
		FROM CrewMember cm
		WHERE cm.userId = :userId AND cm.crew.status = 'RECRUITING' AND cm.crewMemberRole != 'BLOCKED'
		""")
	List<Crew> findAllByUserIdOrderByStatus(@Param("userId") Long userId);

	@Query(value = """
				SELECT COUNT(cm)
				FROM  CrewMember cm
				WHERE cm.crew.id = :crewId AND cm.crewMemberRole != 'BLOCKED'
		""")
	Integer countCrewMemberByCrewId(@Param(value = "crewId") Long crewId);

	@Query(value = """
						SELECT cm
						FROM CrewMember cm
						WHERE cm.crew.id = :crewId AND cm.crewMemberRole != 'BLOCKED'
		""")
	List<CrewMember> findAllByCrewId(@Param(value = "crewId") Long crewId);

	void deleteByUserIdAndCrewId(@Param(value = "userId") Long userId, @Param(value = "crewId") Long crewId);

}
