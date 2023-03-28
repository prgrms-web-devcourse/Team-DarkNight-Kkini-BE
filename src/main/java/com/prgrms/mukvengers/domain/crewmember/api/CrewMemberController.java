package com.prgrms.mukvengers.domain.crewmember.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.mukvengers.domain.crewmember.dto.request.UpdateCrewMemberRequest;
import com.prgrms.mukvengers.domain.crewmember.service.CrewMemberService;
import com.prgrms.mukvengers.global.security.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CrewMemberController {

	private final CrewMemberService crewMemberService;

	@PatchMapping
	@RequestMapping("/api/v1/crews/{crewId}/crewMembers")
	public ResponseEntity<Void> block(
		@RequestBody UpdateCrewMemberRequest updateCrewMemberRequest,
		@PathVariable Long crewId,
		@AuthenticationPrincipal JwtAuthentication user
	) {
		crewMemberService.block(user.id(), updateCrewMemberRequest.blockUserId(), crewId);

		return ResponseEntity.ok().build();

	}

}
