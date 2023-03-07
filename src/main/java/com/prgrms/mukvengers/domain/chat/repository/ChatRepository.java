package com.prgrms.mukvengers.domain.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.chat.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	@Query("""
			SELECT c
			FROM Chat c
			WHERE c.crewId = :crewId
		""")
	Page<Chat> findByCrewId(@Param("crewId") Long crewId, Pageable pageable);
}
