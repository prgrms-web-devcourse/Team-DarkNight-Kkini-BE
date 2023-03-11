package com.prgrms.mukvengers.domain.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse;
import com.prgrms.mukvengers.domain.chat.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	@Query("""
			SELECT 
			new com.prgrms.mukvengers.domain.chat.dto.response.ChatResponse
			(u.id, u.nickname, u.profileImgUrl, c.type, c.createdAt, c.content) 
			FROM Chat c 
			JOIN User u ON u.id = c.userId 
			WHERE c.crewId = :crewId
		""")
	Page<ChatResponse> findByCrewId(@Param("crewId") Long crewId, Pageable pageable);
}
