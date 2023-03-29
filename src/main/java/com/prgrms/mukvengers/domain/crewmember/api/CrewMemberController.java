package com.prgrms.mukvengers.domain.crewmember.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.prgrms.mukvengers.domain.crewmember.dto.request.UpdateCrewMemberRequest;
import com.prgrms.mukvengers.domain.crewmember.service.CrewMemberService;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CrewMemberController {

	private final CrewMemberService crewMemberService;
  
  	/**
	 * <pre>
	 *     모임원 강퇴 기능
	 * </pre>
   	 * @param UpdateCrewMemberRequest 강퇴 하고자 하는 멤버 DTO
	 * @param crewId : 모임 아이디
	 * @param user : 사용자
	 * @return
	 */
	@PatchMapping(value = "/api/v1/crews/{crewId}/crewMembers")
	public ResponseEntity<Void> block(
		@RequestBody UpdateCrewMemberRequest updateCrewMemberRequest,
		@PathVariable Long crewId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		crewMemberService.block(user.id(), updateCrewMemberRequest.blockUserId(), crewId);

		return ResponseEntity.ok().build();
  }

	/**
	 * <pre>
	 *     모임원 삭제 기능
	 * </pre>
	 * @param crewId : 모임 아이디
	 * @param user : 사용자
	 * @return
	 */
	@DeleteMapping(value = "/api/v1/crews/{crewId}/crewMembers")
	public ResponseEntity<Void> delete(
		@PathVariable Long crewId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		crewMemberService.delete(user.id(), crewId);
    
		return ResponseEntity.ok().build();
	}

}
