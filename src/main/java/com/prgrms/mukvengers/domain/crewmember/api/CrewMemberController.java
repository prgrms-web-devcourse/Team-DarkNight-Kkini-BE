package com.prgrms.mukvengers.domain.crewmember.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.mukvengers.domain.crewmember.service.CrewMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CrewMemberController {

	private final CrewMemberService crewMemberService;

	/**
	 * <pre>
	 *     모임원 삭제 기능
	 * </pre>
	 * @param crewId : 모임 아이디
	 * @param crewMemberId : 모임원 아이디
	 * @return
	 */
	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping("/api/v1/crews/{crewId}/crewMembers/{crewMemberId}")
	public ResponseEntity<Void> delete(
		@PathVariable Long crewId,
		@PathVariable Long crewMemberId
	) {
		crewMemberService.delete(crewMemberId);
		return ResponseEntity.ok().build();
	}

}
